/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;

/** @author Sascha Kohlmann */
public class CdiJCommanderSecondCommandTest {
    
    @Test
    public void second_command_test() {
        Application.main("-m", "main", "second", "-c2p", "second");
        assertThat(Application.result, is(equalTo("mainsecond")));
    }
}
