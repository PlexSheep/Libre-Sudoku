package com.kaajjo.libresudoku.core.utils

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException


// File type from OpenSudoku app (https://gitlab.com/opensudoku/opensudoku)
// https://gitlab.com/opensudoku/opensudoku/-/blob/develop/app/src/main/java/org/moire/opensudoku/gui/importing/OpenSudokuImportTask.java
/**
 * .opensudoku - format from the OpenSudoku app. Uses XML schema
 */
class OpenSudokuParser {
    private val tag = "OpenSudokuParser"

    /**
     * @param text .opensudoku file content
     * @return Pair with: First - parsing success. Second - strings of parsed boards
     */
    fun textToStringBoards(text: String): Pair<Boolean, List<String>> {
        var result: Pair<Boolean, List<String>> = Pair(false, emptyList())

        val factory: XmlPullParserFactory
        val xpp: XmlPullParser
        try {
            factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = false
            xpp = factory.newPullParser()
            xpp.setInput(text.reader())
            var eventType = xpp.eventType
            var rootTag: String
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    rootTag = xpp.name
                    if (rootTag == "opensudoku") {
                        val version = xpp.getAttributeValue(null, "version")
                        if (version == null) {
                            // no version provided, assume that it's version 1
                            result = importV1(xpp)
                        } else if (version == "2") {
                            result = importV2(xpp)
                        }
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            return result.copy(first = false)
        } catch (e: IOException) {
            e.printStackTrace()
            return result.copy(first = false)
        }
        return result
    }

    private fun importV1(parser: XmlPullParser): Pair<Boolean, List<String>> {
        val boards = mutableListOf<String>()
        var eventType = parser.eventType
        var lastTag = ""
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                lastTag = parser.name
                if (lastTag == "game") {
                    val boardString = parser.getAttributeValue(null, "data")
                    if (boardString.length == 81) {
                        boards.add(boardString)
                    } else {
                        Log.i("$tag/ImportV1", "This line was skipped $boardString")
                    }
                }
            }
            eventType = parser.next()
        }
        return Pair(true, boards)
    }


    private fun importV2(parser: XmlPullParser): Pair<Boolean, List<String>> {
        var eventType = parser.eventType
        var lastTag = ""
        //var folderName: String? = null
        val boards = mutableListOf<String>()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                lastTag = parser.name
                if (lastTag == "folder") {
                    // folderName = parser.getAttributeValue(null, "name")
                } else if (lastTag == "game") {
                    // Not used now, but maybe will be used in future
                    // val created = parseLong(parser.getAttributeValue(null, "created"), System.currentTimeMillis());
                    // val lastPlayed = parseLong(parser.getAttributeValue(null, "last_played"), 0);
                    // val note = parser.getAttributeValue(null, "note");
                    // val state = parseLong(parser.getAttributeValue(null, "state"))
                    // val timer = parseLong(parser.getAttributeValue(null, "time"), 0)

                    val boardString = parser.getAttributeValue(null, "data")
                    if (boardString.length == 81) {
                        boards.add(boardString)
                    } else {
                        Log.i("$tag/ImportV2", "This line was skipped $boardString")
                    }
                }
            }
            eventType = parser.next()
        }
        return Pair(true, boards)
    }
}