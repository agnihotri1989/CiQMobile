package com.crawford.ciq.dev

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

//      val gettempleobject =  gettemplatejsonobejct("string",0,"string", listOf("string","string"),
//            0,"string","string")
//
//    val finaloutput =    submitdocketjson("string","string","string","string","string",0,
//        0,0,0,"string",true,0,0,"string",
//        0,0,"string",gettempleobject)
//
//        val debugoutput = finaloutput
        val l = LocalDate.parse("14-02-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        val unix = l.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val parser = SimpleDateFormat("MM/dd/yyyy HH:mm:ss a")

        //val output: String = formatter.format(parser.parse("12/9/2020 08:00:00 PM"))
        val output: String = formatter.format(parser.parse("12/9/2020 06:35:00 AM"))

        assertEquals(4, 2 + 2)
    }

    fun submitdocketjson(
        claimID: String, branchID: String, date: String, adjusterID: String,
        timeCode: String, claimantId: Int, docketRate: Int, claimAssistID: Int,
        expenseAmount: Int, expenseCode: String, expenseEmployeeReimbursable: Boolean,
        expenseRatePerUnit: Int, expenseUnits: Int, note: String, timeAmount: Int, timeUnits: Int,
        templateCode: String, templatejsonobject: JsonObject

    ): JsonObject {
        val parentjsonobject = JsonObject()
        parentjsonobject.addProperty("claimID", claimID)
        parentjsonobject.addProperty("branchID", branchID)
        parentjsonobject.addProperty("date", date)
        parentjsonobject.addProperty("adjusterID", adjusterID)
        parentjsonobject.addProperty("timeCode", timeCode)
        parentjsonobject.addProperty("claimantId", claimantId)
        parentjsonobject.addProperty("docketRate", docketRate)
        parentjsonobject.addProperty("claimAssistID", claimAssistID)
        parentjsonobject.addProperty("expenseAmount", expenseAmount)
        parentjsonobject.addProperty("expenseCode", expenseCode)
        parentjsonobject.addProperty("expenseEmployeeReimbursable", expenseEmployeeReimbursable)
        parentjsonobject.addProperty("expenseRatePerUnit", expenseRatePerUnit)
        parentjsonobject.addProperty("expenseUnits", expenseUnits)
        parentjsonobject.addProperty("note", note)
        parentjsonobject.addProperty("timeAmount", timeAmount)
        parentjsonobject.addProperty("timeUnits", timeUnits)
        parentjsonobject.add("templatePayload", templatejsonobject)


        return parentjsonobject
    }

    fun gettemplatejsonobejct(
        templateCode: String, templateFieldId: Int, templateFieldName: String,
        templateFieldValue: List<String>, sequenceId: Int, dataType: String, sourceType: String
    ): JsonObject {

        val parentjsonobejct = JsonObject()
        parentjsonobejct.addProperty("templateCode", templateCode)
        val parentjsonarray = JsonArray()
        val childjsonopbject = JsonObject()
        childjsonopbject.addProperty("templateFieldId", templateFieldId)
        childjsonopbject.addProperty("templateFieldName", templateFieldName)
        childjsonopbject.add(
            "templateFieldValue",
            Gson().toJsonTree(templateFieldValue).asJsonArray
        )
        childjsonopbject.addProperty("sequenceId", sequenceId)
        childjsonopbject.addProperty("dataType", dataType)
        childjsonopbject.addProperty("sourceType", sourceType)

        parentjsonarray.add(childjsonopbject)
        parentjsonobejct.add("templateFields", parentjsonarray)
        return parentjsonobejct


    }
}