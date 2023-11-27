package com.czhao.test.jdk18;

/**
 * @author zhaochun
 *
 */
public class TestSnippet {

    /**
     * testCodeInPre
     *
     * <P>Use Sample:</P>
     * <pre>{@code
     *     TestSnippet testSnippet = new TestSnippet();
     *     // call testCodeInPre
     *     testSnippet.testCodeInPre();
     *     List<String> list = new ArrayList<>();
     * }</pre>
     *
     */
    public void testCodeInPre() {
        System.out.println("testCodeInPre...");
    }

    /**
     * testSnippet
     *
     * <P>Use Sample:</P>
     * {@snippet :
     *     TestSnippet testSnippet = new TestSnippet();
     *     // call testSnippet
     *     testSnippet.testSnippet();
     *     List<String> list = new ArrayList<>();
     * }
     *
     */
    public void testSnippet() {
        System.out.println("testSnippet...");
    }

    /**
     * testSnippetHighlight
     *
     * <P>Use Sample:</P>
     * {@snippet :
     *     TestSnippet testSnippet = new TestSnippet(); // @highlight region regex = "\btestSnippet\b"
     *     // call testSnippetHighlight
     *     testSnippet.testSnippetHighlight(); // @highlight substring="testSnippetHighlight()"
     *     testSnippet.testCodeInPre();
     *     List<String> list = new ArrayList<>(); // @end
     * }
     *
     */
    public void testSnippetHighlight() {
        System.out.println("testSnippetHighlight...");
    }

    /**
     * The following code shows how to use {@code Optional.isPresent}:
     * {@snippet file="ShowOptional.java" region="example"}
     */
    public void showOptional() {
        System.out.println("showOptional...");
    }

}
