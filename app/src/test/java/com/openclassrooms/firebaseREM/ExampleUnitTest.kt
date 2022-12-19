package com.openclassrooms.firebaseREM

import com.openclassrooms.firebaseREM.Utils.convertChoiceMonthIntoDate
import com.openclassrooms.firebaseREM.Utils.convertEuroToDollar
import com.openclassrooms.firebaseREM.Utils.todayDateFrenchFormat
import org.junit.Assert
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun convertEuroToDollarTest() {
        val euro = 1000
        val dollar = 1060
        Assert.assertEquals(dollar, convertEuroToDollar(euro))
    }

    @Test
    fun dateReformedTest() {
        Assert.assertEquals("2022/11/21", todayDateFrenchFormat)
    }

    @Test
    fun whenDateIsEmptyReturn25Test() {
        Assert.assertEquals(Utils.monthsBetweenTwoDates("2022/07/22", ""), 25)
    }

    @Test
    fun whenDateIsEmptyReturn24Test() {
        Assert.assertEquals(Utils.monthsBetweenTwoDates("2024/07/22", "2022/07/22"), 24)
    }

    @Test
    fun testUnderstandHowWorkDateString() {
        Assert.assertTrue("08/11/2022" < "09/11/2021")
        Assert.assertTrue("2022/11/08" > "2021/11/09")
        Assert.assertTrue("2022/10/10" < "2022/11/09")
    }

    @Test
    fun testConvertIntIntoDate() {
        val string = convertChoiceMonthIntoDate(2)
        Assert.assertTrue(string == "2022/09/21")
    }

}