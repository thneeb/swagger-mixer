package de.neebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SwaggerMixer.class, ObjectMapper.class})
public class SwaggerMixerTest {
    @Autowired
    private SwaggerMixer swaggerMixer;

    @Rule
    public OutputCaptureRule outputCapture = new OutputCaptureRule();

    @Test
    public void testProductCatalog() throws Exception {
        swaggerMixer.run("../../testfiles/compose.json");
        Assert.assertTrue(outputCapture.getOut().contains("Hello World"));
    }
}
