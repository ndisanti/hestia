package ar.com.despegar.p13n.hestia.test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Init all class fields annotated with {@link Mock}
 *
 * @author jcastro
 * @since Dec 7, 2012
 */
public abstract class MockitoAnnotationBaseTest {


    public MockitoAnnotationBaseTest() {
        MockitoAnnotations.initMocks(this);
    }

}
