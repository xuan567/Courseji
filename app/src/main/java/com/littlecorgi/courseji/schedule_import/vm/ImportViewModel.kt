package com.littlecorgi.courseji.schedule_import.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.littlecorgi.courseji.AppDatabase
import com.littlecorgi.courseji.schedule.logic.model.bean.TableBean
import com.littlecorgi.courseji.schedule_import.Common
import com.littlecorgi.courseji.schedule_import.logic.parse.ZFParse

/**
 *
 * @author littlecorgi 2021/1/9
 */
class ImportViewModel(application: Application) : AndroidViewModel(application) {

    var importId = -1
    var school: String? = null
    var importType: String? = null

    var newFlag = false
    var zfType = 0

    private val dataBase = AppDatabase.getDatabase(application)
    private val tableDao = dataBase.tableDao()
    private val courseDao = dataBase.courseDao()

    suspend fun importSchedule(source: String): Int {
        val parser = when (importType) {
            Common.TYPE_ZF_NEW -> ZFParse(source)
            else -> null
        }
        Log.d("ImportViewModel", "importSchedule: $parser ${importType ?: "1234"}")
        Log.d("ImportViewModel", "importSchedule1234: $newFlag $importId 1234")
        return parser?.saveCourse(getApplication(), importId) { baseList, detailList ->
            if (!newFlag) {
                courseDao.coverImport(baseList, detailList)
            } else {
                tableDao.insertTable(TableBean(id = importId, tableName = "未命名"))
                courseDao.insertCourses(baseList, detailList)
            }
        } ?: throw Exception("请确保选择正确的教务类型，以及到达显示课程的页面")
    }

    suspend fun getNewId(): Int {
        val lastId = tableDao.getLastId()
        return if (lastId != null) lastId + 1 else 1
    }

    private fun getIndexSum(start: Int, end: Int): Int {
        var value = 0
        for (i in start..end) {
            value += (1 shl i)
        }
        return value
    }
}
