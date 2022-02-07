package com.seif.todoit.veiwModel

import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.seif.todoit.data.local.TodoDatabase
import com.seif.todoit.data.models.TodoModel
import com.seif.todoit.data.repository.RepositoryImplementation
import com.seif.todoit.ui.fragments.ToDoListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getInstance(application).todoDao()
    private val repository: RepositoryImplementation = RepositoryImplementation(todoDao)
    val getAllToDos: LiveData<List<TodoModel>> = todoDao.getAllToDos()

    private var installStateUpdatedListener: InstallStateUpdatedListener?=null
    private var appUpdateManager:AppUpdateManager?=null
    private val updateStateMessage = MutableLiveData<String>()
    private val TAG = "HomeViewModel"
    private val REQ_CODE_UPDATE_VERSION = 330
    fun insertTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todoModel)
        }
    }

    fun updateTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todoModel)
        }
    }

    fun deleteTodo(todoModel: TodoModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todoModel)
        }
    }

    fun deleteAllToDos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllToDos()
        }
    }

    fun searchTodo(searchQuery: String):LiveData<List<TodoModel>>{
       return repository.searchTodo(searchQuery)
    }

    fun sortByPriorityHigh():LiveData<List<TodoModel>>{
        return repository.sortByPriorityHigh()
    }

    fun sortByPriorityLow():LiveData<List<TodoModel>>{
        return repository.sortByPriorityLow()
    }

    fun checkForAppUpdate(context: Context) {
        if (isNetworkConnected(context) ){
            Log.d(TAG, "checkForAppUpdate: ")
            appUpdateManager = AppUpdateManagerFactory.create(context)
            val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
            installStateUpdatedListener =
                InstallStateUpdatedListener { installState: InstallState ->
                    if (installState.installStatus() == InstallStatus.DOWNLOADED)
                        popupSnackBarForCompleteUpdateAndUnregister()
                    else Log.d(TAG, "checkForAppUpdate: app updated")
                }
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                Log.d(TAG, "checkForAppUpdate: ${appUpdateInfo.packageName() + appUpdateInfo.availableVersionCode()}")
                Log.d(TAG, "checkForAppUpdate: ${appUpdateInfo.updateAvailability().toString() + UpdateAvailability.UPDATE_AVAILABLE.toString()}")
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        appUpdateManager!!.registerListener(installStateUpdatedListener!!)
                        startAppUpdateFlexible(appUpdateInfo)
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startAppUpdateImmediate(appUpdateInfo)
                    }
                }
            }
        }

    }
    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,  // The current activity making the update request.
                ToDoListFragment().requireActivity(),  // Include a request code to later monitor this update request.
                REQ_CODE_UPDATE_VERSION
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
            unregisterInstallStateUpdListener()
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager!!.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,  // The current activity making the update request
                ToDoListFragment().requireActivity(),
                REQ_CODE_UPDATE_VERSION
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun popupSnackBarForCompleteUpdateAndUnregister() {
        updateStateMessage.value = "An update has just been downloaded."
    }

    private fun unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null) appUpdateManager!!.unregisterListener(
            installStateUpdatedListener!!
        )
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isInternet =  cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        Log.d(TAG, "isNetworkConnected: $isInternet")
        return isInternet
    }
}