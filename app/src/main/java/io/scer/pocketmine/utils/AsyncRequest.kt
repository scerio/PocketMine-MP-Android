package io.scer.pocketmine.utils

import android.os.AsyncTask
import org.json.JSONObject
import java.net.URL
import kotlin.collections.HashMap

class AsyncRequest : AsyncTask<String, Void, HashMap<String, JSONObject>?>() {

    override fun doInBackground(vararg channels: String?): HashMap<String, JSONObject>? {
        val map = HashMap<String, JSONObject>()
        channels.forEach { channel ->
            try {
                map[channel.toString()] = JSONObject(URL("https://update.pmmp.io/api?channel=$channel").readText())
            } catch (e: Exception) {
                try {
                    val json = JSONObject(URL("https://jenkins.pmmp.io/job/PocketMine-MP/${channel!!.capitalize()}/artifact/build_info.json").readText())
                    json.put("download_url", "https://jenkins.pmmp.io/job/PocketMine-MP/${json.get("build_number")}/artifact/PocketMine-MP.phar")
                    map[channel.toString()] = json
                } catch (e: Exception) {
                    return null //Network error or pmmp.io is down
                }
            }
        }
        return map
    }
}