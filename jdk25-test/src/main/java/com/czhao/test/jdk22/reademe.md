openjdk22
==========

openJDK22中转正的新特性有:
- JEP 423: Region Pinning for G1
- JEP 454: Foreign Function & Memory API
- JEP 456: Unnamed Variables & Patterns
- JEP 458: Launch Multi-File Source-Code Programs

## JEP 456: Unnamed Variables & Patterns
未命名变量与模式,JDK22转正的新特性。
> 终于转正了，其他很多语言早就支持了，比如golang,scala,kotlin,python,js/ts等等

### 摘要
通过未命名变量和未命名模式增强 Java 编程语言，这些特性可用于需要变量声明或嵌套模式但从未使用的场景。两者均用下划线字符 “_” 表示。

### 历史
未命名变量和未命名模式在JDK 21中通过JEP 443（标题为"未命名模式和变量"）首次预览。我们在此提议不做更改地正式确定该特性。

### 目标
1. **明确未使用变量意图**：通过语法标记（如下划线`_`）显式声明绑定变量或Lambda参数不被使用，并在编译期强制验证，从而提升代码清晰度并减少潜在错误。
2. **增强代码可维护性**：识别必须声明但实际未使用的变量（如catch子句中的异常参数），通过静态检查确保代码简洁性。
3. **支持多模式匹配**：允许在单个case标签中使用多个不声明变量的模式（如`case _, _ ->`），简化逻辑分支。
4. **优化记录模式可读性**：通过省略不必要的嵌套类型模式（如`Point(_, _)`），提升复杂数据结构匹配的简洁性。

### 非目标范围
- 其目标不包括允许未命名字段或方法参数。
- 其目标也不包括修改局部变量在确定赋值分析等场景中的语义。

### 动机
开发人员有时会声明不打算使用的变量，这可能是出于代码风格的考虑，也可能是因为语言要求在某些上下文中必须进行变量声明。不使用变量的意图在编写代码时是明确的，但如果没有显式体现这一意图，后续维护人员可能会意外使用该变量，从而违背原本的设计意图。如果我们能让这类变量无法被意外使用，代码将更具信息性、可读性，且更不容易出错。


#### 未使用的变量
在代码中，当副作用比结果更重要时，声明从未使用的变量的需求尤为常见。例如，以下代码通过循环的副作用计算`total`，而不使用循环变量`order`：

```java
static int count(Iterable<Order> orders) {
    int total = 0;
    for (Order order : orders)    // order is unused
        total++;
    return total;
}
```  
鉴于`order`未被使用，其声明的突出性显得多余。虽然可以将声明简化为`var order`，但无法避免为变量命名。即使将名称缩短为`o`等简称，这种语法技巧也无法传达“变量永不使用”的意图。此外，静态分析工具通常会对未使用的变量发出警告，即使开发者明确不使用该变量，也可能无法消除警告。

另一个例子中，表达式的副作用比结果更重要：以下代码从队列中出队数据，但每三个元素仅需两个：

```java
Queue<Integer> q = ... // x1, y1, z1, x2, y2, z2 ..
while (q.size() >= 3) {
   int x = q.remove();
   int y = q.remove();
   int z = q.remove();            // z is unused
    ... new Point(x, y) ...
}
```  
第三次调用`remove()`的目标是副作用（出队元素），无论其结果是否赋值给变量，因此`z`的声明可以省略。但为了可维护性，代码作者可能希望通过声明变量来统一表示`remove()`的结果。目前有两种不理想的选择：
1. 不声明变量`z`，这会导致代码结构不对称，并可能触发静态分析工具对“忽略返回值”的警告；
2. 声明未使用的变量`z`，可能触发“未使用变量”的静态分析警告。

未使用的变量还常见于以下两种侧重副作用的语句中：
1. **try-with-resources 语句**：该语句仅用于副作用（自动关闭资源）。在某些情况下，资源代表try块代码执行的上下文，但代码并不直接使用该上下文，因此资源变量的名称无关紧要。例如，假设`ScopedContext`是`AutoCloseable`资源：
   ```java
    try (var acquiredContext = ScopedContext.acquire()) {
        ... acquiredContext not used ...
    }
   ```  
   名称`acquiredContext`纯属冗余，若能省略会更简洁。

2. **异常处理**：处理异常时经常会产生未使用的变量。例如，大多数开发者可能编写过以下形式的catch块，其中异常参数`ex`未被使用：
   ```java
    String s = ...;
    try {
        int i = Integer.parseInt(s);
        ... i ...
    } catch (NumberFormatException ex) {
        System.out.println("Bad number: " + s);
    }
   ```  

即使无副作用的代码有时也必须声明未使用的变量。例如：
```java
...stream.collect(Collectors.toMap(String::toUpperCase,
                                   v -> "NODATA"));
```  
这段代码生成一个映射，每个键对应相同的占位值。由于lambda参数`v`未被使用，其名称无关紧要。

在所有这些场景中，变量未被使用且名称无关紧要，若能直接声明**无名称变量**会更理想。这将使维护人员无需理解无关名称，并避免静态分析工具对“未使用变量”的误报。

**可合理声明为无名称的变量类型**包括：方法内无可见性的变量（局部变量、异常参数、lambda参数，如上述示例）。这些变量可重命名或设为无名称，且不会影响外部逻辑。相比之下，字段（即使是私有字段）用于跨方法传递对象状态，无名称的状态既无帮助也难以维护。


#### 未使用的模式变量
局部变量也可以通过**类型模式**声明（这类局部变量称为“模式变量”），因此类型模式也可能声明未使用的变量。考虑以下代码，它在对密封类`Ball`实例进行判断的`switch`语句的`case`标签中使用了类型模式：

```java
sealed abstract class Ball permits RedBall, BlueBall, GreenBall { }
final  class RedBall   extends Ball { }
final  class BlueBall  extends Ball { }
final  class GreenBall extends Ball { }

Ball ball = ...
switch (ball) {
    case RedBall   red   -> process(ball);
    case BlueBall  blue  -> process(ball);
    case GreenBall green -> stopProcessing();
}
```  
在`switch`的各个`case`中，通过类型模式检查`Ball`的具体类型，但模式变量`red`、`blue`和`green`并未在`case`子句的右侧使用。如果能省略这些变量名，代码会更简洁清晰。

现在假设我们定义一个`Box`记录类，它可以持有任意类型的`Ball`，也可能持有`null`值：

```java
record Box<T extends Ball>(T content) { }

Box<? extends Ball> box = ...
switch (box) {
    case Box(RedBall   red)     -> processBox(box);
    case Box(BlueBall  blue)    -> processBox(box);
    case Box(GreenBall green)   -> stopProcessing();
    case Box(var       itsNull) -> pickAnotherBox();
}
```  
这里的嵌套类型模式仍然声明了未使用的模式变量。由于这个`switch`语句比前一个更复杂，省略嵌套类型模式中未使用变量的名称将进一步提升可读性。


#### 未使用的嵌套模式
我们可以在记录（record）中嵌套记录，这会导致数据结构的形状与其内部的数据项同等重要。例如：

```java
record Point(int x, int y) { }
enum Color { RED, GREEN, BLUE }
record ColoredPoint(Point p, Color c) { }

... new ColoredPoint(new Point(3,4), Color.GREEN) ...

if (r instanceof ColoredPoint(Point p, Color c)) {
    ... p.x() ... p.y() ...
}
```
在此代码中，程序的一部分创建了`ColoredPoint`实例，另一部分通过`instanceof`模式判断变量是否为`ColoredPoint`，并提取其两个组件值。  
像`ColoredPoint(Point p, Color c)`这样的记录模式具有描述性，但程序通常仅使用部分组件值进行后续处理。例如，上述`if`块中仅使用了`p`，未使用`c`。每次进行此类模式匹配时，为记录类的所有组件编写类型模式显得繁琐。此外，代码中未明确体现`Color`组件完全无关，这也导致`if`条件的可读性下降。当记录模式嵌套以提取组件内部数据时，这一问题尤为明显，例如：

```java
if (r instanceof ColoredPoint(Point(int x, int y), Color c)) {
    ... x ... y ...
}
```

我们可以使用未命名模式变量来减少视觉负担，例如`ColoredPoint(Point(int x, int y), Color _)`，但类型模式中的`Color`类型仍显冗余。也可以通过`var`简化，如`ColoredPoint(Point(int x, int y), var _)`，但嵌套的`var _`模式仍显繁琐。更好的方式是完全省略不必要的组件，进一步降低视觉负担。这不仅能简化记录模式的编写，还能通过去除代码冗余提升可读性。

### 描述
未命名变量的声明方式是在局部变量声明语句中使用下划线字符 _（U+005F）代替局部变量名，或在 catch 子句中代替异常参数名，或在 lambda 表达式中代替 lambda 参数名。

未命名模式变量的声明方式是在类型模式中使用下划线字符代替模式变量名。

未命名模式由下划线字符表示，其功能等同于未命名的类型模式 var _。该模式允许在模式匹配中省略记录组件的类型和名称。

单一下划线字符是最简洁的语法形式，用于表示名称的缺失。这种用法在其他语言（如 Scala 和 Python）中也很常见。虽然单下划线在 Java 1.0 时期曾是合法标识符，但后来我们将其回收用于未命名变量和模式：自 Java 8（2014年）起，当使用下划线作为标识符时会触发编译时警告；而在 Java 9（2017年，JEP 213）中，我们直接从语言规范移除了此类标识符，使相关警告升级为错误。

对于两个及以上字符长度的标识符，下划线的使用规则保持不变——因为下划线仍是合法的Java字母及Java数字字母。例如 _age、MAX_AGE 和 __（双下划线）这类标识符仍然有效。

下划线作为数字分隔符的功能也未改变。诸如 123_456_789 和 0b1010_0101 这类数字字面量仍然合法。

**未命名变量**  
以下类型的声明可以引入**具名变量**（用标识符表示）或**未命名变量**（用下划线 `_` 表示）：

1. 代码块中的局部变量声明语句（JLS §14.4.2）
2. `try-with-resources` 语句的资源声明（JLS §14.20.3）
3. 基本 `for` 循环的头部（JLS §14.14.1）
4. 增强 `for` 循环的头部（JLS §14.14.2）
5. `catch` 块的异常参数（JLS §14.20）
6. Lambda 表达式的形式参数（JLS §15.27.1）

声明未命名变量不会在作用域中引入名称，因此该变量在初始化后既不能被写入，也不能被读取。在局部变量声明或 `try-with-resources` 语句中声明未命名变量时，必须提供初始化表达式。

由于未命名变量没有名称，因此它**不会遮蔽任何其他变量**，同一代码块中可以声明多个未命名变量。

以下是上述示例改用未命名变量的改写版本：

- 带有副作用的增强 `for` 循环
```java
static int count(Iterable<Order> orders) {
    int total = 0;
    for (Order _ : orders)    // Unnamed variable
        total++;
    return total;
}
```  

- 基本 `for` 循环的初始化部分也可声明未命名局部变量
```java
for (int i = 0, _ = sideEffect(); i < 10; i++) { ... i ... }
```  

- 赋值语句（右侧表达式的结果不需要时）
```java
Queue<Integer> q = ... // x1, y1, z1, x2, y2, z2, ...
while (q.size() >= 3) {
   var x = q.remove();
   var y = q.remove();
   var _ = q.remove();        // Unnamed variable
   ... new Point(x, y) ...
}
```  

如果程序只需处理 `x1, x2` 等坐标，可以在多个赋值语句中使用未命名变量：
```java
while (q.size() >= 3) {
    var x = q.remove();
    var _ = q.remove();       // Unnamed variable
    var _ = q.remove();       // Unnamed variable
    ... new Point(x, 0) ...
}
```  

- `catch` 块
```java
String s = ...
try {
    int i = Integer.parseInt(s);
    ... i ...
} catch (NumberFormatException _) {        // Unnamed variable
    System.out.println("Bad number: " + s);
}
```  

未命名变量可用于多个 `catch` 块：
```java
try { ... }
catch (Exception _) { ... }                // Unnamed variable
catch (Throwable _) { ... }                // Unnamed variable
```  

- `try-with-resources`
```java
try (var _ = ScopedContext.acquire()) {    // Unnamed variable
    ... no use of acquired resource ...
}
```  

- Lambda 表达式的参数无用时
```java
...stream.collect(Collectors.toMap(String::toUpperCase,
                                   _ -> "NODATA"))    // Unnamed variable
```


**未命名模式变量**  
未命名模式变量可以出现在类型模式（JLS §14.30.1）中，包括 `var` 类型模式，无论该类型模式是顶层模式还是嵌套在记录模式中。例如，原来的 `Ball` 示例现在可以写成：

```java
switch (ball) {
    case RedBall _   -> process(ball);          // Unnamed pattern variable
    case BlueBall _  -> process(ball);          // Unnamed pattern variable
    case GreenBall _ -> stopProcessing();       // Unnamed pattern variable
}
```  

而 `Box` 和 `Ball` 的示例可以改写为：

```java
switch (box) {
    case Box(RedBall _)   -> processBox(box);   // Unnamed pattern variable
    case Box(BlueBall _)  -> processBox(box);   // Unnamed pattern variable
    case Box(GreenBall _) -> stopProcessing();  // Unnamed pattern variable
    case Box(var _)       -> pickAnotherBox();  // Unnamed pattern variable
}
```  

通过允许我们省略名称，未命名模式变量使得基于类型模式的运行时数据探索（无论是在 `switch` 语句块中还是与 `instanceof` 运算符配合使用）在视觉上更加清晰。


**多重模式匹配的case标签**
当前case标签限制最多只能包含一个模式。随着未命名模式变量和未命名模式的引入，我们更可能遇到在同一个switch块中出现多个模式不同但右侧处理逻辑相同的case子句。例如在Box和Ball示例中，前两个子句具有相同的右侧逻辑但模式不同：

```java
switch (box) {
    case Box(RedBall _)   -> processBox(box);
    case Box(BlueBall _)  -> processBox(box);
    case Box(GreenBall _) -> stopProcessing();
    case Box(var _)       -> pickAnotherBox();
}
```

我们可以通过允许前两个模式出现在同一个case标签中来简化代码：

```java
switch (box) {
    case Box(RedBall _), Box(BlueBall _) -> processBox(box);
    case Box(GreenBall _)                -> stopProcessing();
    case Box(var _)                      -> pickAnotherBox();
}
```

因此，我们修改switch标签的语法规则（JLS §14.11.1）为：

```
SwitchLabel:
    case CaseConstant {, CaseConstant}
    case null [, default]
    case CasePattern {, CasePattern } [Guard]
    default
```

并定义具有多个模式的case标签的语义为：只要值匹配其中任意一个模式就算匹配成功。

如果case标签包含多个模式，那么其中任何一个模式声明了模式变量都会导致编译时错误。

具有多个case模式的case标签可以包含一个守卫条件。该守卫条件作用于整个case，而不是单个模式。例如，假设存在一个int变量x，前面示例的第一个case可以进一步约束：

```java
case Box(RedBall _), Box(BlueBall _) when x == 42 -> processBox(b);
```

守卫条件是case标签的属性，而不是case标签内单个模式的属性，因此禁止编写多个守卫条件：

```java
case Box(RedBall _) when x == 0, Box(BlueBall _) when x == 42 -> processBox(b);
    // compile-time error
```


**未命名模式**
未命名模式是一种无条件匹配任何值但**不声明也不初始化任何变量**的模式。与未命名的类型模式 `var _` 类似，未命名模式可以嵌套在记录模式中，但**不能**作为顶层模式使用（例如在 `instanceof` 表达式或 `case` 标签中）。

因此，之前的示例可以完全省略 `Color` 组件的类型模式：
```java
if (r instanceof ColoredPoint(Point(int x, int y), _)) { ... x ... y ... }
```  

同样，我们可以在提取 `Color` 组件值时省略 `Point` 组件的记录模式：
```java
if (r instanceof ColoredPoint(_, Color c)) { ... c ... }
```  

在深层嵌套的场景中，使用未命名模式能提升复杂数据提取代码的可读性。例如：
```java
if (r instanceof ColoredPoint(Point(int x, _), _)) { ... x ... }
```  
这段代码提取了嵌套 `Point` 中的 `x` 坐标，同时明确表明不提取 `y` 和 `Color` 组件的值。

回到 `Box` 和 `Ball` 的示例，我们可以用未命名模式 `_` 替代 `var _` 进一步简化最后的 `case` 标签：
```java
switch (box) {
    case Box(RedBall _), Box(BlueBall _) -> processBox(box);
    case Box(GreenBall _)                -> stopProcessing();
    case Box(_)                          -> pickAnotherBox();
}
```


### 风险与假设
我们假设当前活跃维护的代码中极少（甚至没有）使用下划线作为变量名。那些从Java 7直接迁移到Java 22、且未经历过Java 8警告或Java 9错误提示的开发者可能会感到意外。他们在读写名为`_`的变量，或声明其他名为`_`的元素（类、字段等）时，将面临编译错误的风险。

我们预期静态分析工具的开发者能理解下划线在未命名变量中的新角色，并避免在现代代码中对这类变量的未使用情况发出警告。

### 替代方案
理论上可以定义类似的未命名方法参数概念。但这会与规范产生微妙交互（例如：重写带有未命名参数的方法意味着什么？），也会影响工具链（例如：如何为未命名参数编写JavaDoc？）。这可能会成为未来JEP的议题。

JEP 302（Lambda剩余问题）曾研究过未使用的lambda参数问题，并提出用下划线表示这些参数，但该提案还包含许多通过其他方式更好解决的问题。本JEP解决了JEP 302中探讨的未使用lambda参数问题，但未涉及该提案中的其他议题。

