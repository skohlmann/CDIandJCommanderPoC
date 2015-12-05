/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/** @author Sascha Kohlmann */
public class CdiJCommanderFirstCommandTest {

    @Test
    public void first_command_test() {
        Application.main("-m", "rule", "first", "-c1p", "first");
        assertThat(Application.result, is(equalTo("rulefirst")));
    }
}
