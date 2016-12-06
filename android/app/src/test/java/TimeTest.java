/**
 * Created by david on 12/4/16.
 */

import com.maptest.GeofenceAndroid.GeofenceReceiver;
import com.maptest.Utils.TimeParser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import static com.maptest.Utils.TimeParser.parseMin;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeTest {

    @Test
    public void testTimeToMinute() throws ParseException, IllegalAccessException, InstantiationException {
        TimeParser.TimeHandler mockedCalendar = mock(TimeParser.TimeHandler.class);
        // Get new Calendar object from function to avoid getting the exact same object
        when(mockedCalendar.getNewObject()).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock onMock) throws ParseException {
                return getTestTime(new SimpleDateFormat("hh:mm").parse("09:00"));
            }
        });
        TimeParser.handler = mockedCalendar;
        TimeResult[] timeStamps = new TimeResult[]{new TimeResult("10:02", 62),new TimeResult("nu", 0),new TimeResult("5 min", 5),new TimeResult("01:00", 960)};
        // Check that all test cases in the array is correct
        for (TimeResult tr : timeStamps) {
            Assert.assertThat(parseMin(tr.getTime()), is(tr.getMin()));
        }

    }

    private Calendar getTestTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    class TimeResult {
        private String time;

        public int getMin() {
            return min;
        }

        public String getTime() {
            return time;
        }

        private int min;

        public TimeResult(String time, int min) {
            this.time = time;
            this.min = min;
        }
    }
}
