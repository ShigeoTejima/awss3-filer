package jp.gr.java_conf.pekokun.awss3_filer.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class FileObjectTest {

    @Test
    public void test_isFolder() {
        assertThat(new FileObject("bucket", "foo/", "owner").isFolder(), is(true));
        assertThat(new FileObject("bucket", "foo/bar", "owner").isFolder(), is(false));
    }

    @Test
    public void test_getName() {
        assertThat(new FileObject("bucket", null, "owner").getName(), is(""));
        assertThat(new FileObject("bucket", "", "owner").getName(), is(""));
        assertThat(new FileObject("bucket", "foo/", "owner").getName(), is("foo"));
        assertThat(new FileObject("bucket", "foo/bar", "owner").getName(), is("bar"));
    }
}
