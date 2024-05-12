package com.example.nntodo

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.nntodo.databinding.FragmentNewTaskSheetBinding
import com.example.nntodo.databinding.TaskItemCellBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment(),
    AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskItemCellBinding: TaskItemCellBinding

    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()


        if (taskItem != null) {
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.taskDescription.text = editable.newEditable(taskItem!!.description)

            if (taskItem!!.dueTime() != null){
                dueTime = taskItem!!.dueTime()!!
                updateTimeButton()
            }

        } else {
            binding.taskTitle.text = "New Task"
        }


        taskViewModel = ViewModelProvider(activity)[TaskViewModel::class.java]
        binding.save.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        if (dueTime == null) {
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener{_, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButton()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due Time")
        dialog.show()
    }

    @SuppressLint("DefaultLocale")
    private fun updateTimeButton() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val description = binding.taskDescription.text.toString()
        val dueTimeString = if (dueTime == null) null else TaskItem.timeFormatter.format(dueTime)

        if (taskItem == null) {
            val newTask = TaskItem(name, description, dueTimeString, null)
            taskViewModel.addTaskItem(newTask)
        } else {
            taskItem!!.name = name
            taskItem!!.description = description
            taskItem!!.dueTimeString = dueTimeString
            taskViewModel.updateTaskItem(taskItem!!)
        }
        binding.name.setText("")
        binding.taskDescription.setText("")
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        taskItemCellBinding = TaskItemCellBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Handle item selection
        val selectedPriority = parent?.getItemAtPosition(position) as String

        taskItemCellBinding = TaskItemCellBinding.inflate(layoutInflater)
        // Access views from task_item_cell.xml through its binding
        val cellItemLinearMain = taskItemCellBinding.taskCellContainer

        // Update the selected priority in the view model
        when (selectedPriority) {
            "Low" -> {
                val color = ContextCompat.getColor(requireContext(), R.color.low_priority_color)
                cellItemLinearMain.setBackgroundColor(color)
            }
            "Medium" -> {
                val color = ContextCompat.getColor(requireContext(), R.color.medium_priority_color)
                cellItemLinearMain.setBackgroundColor(color)
            }
            "High" -> {
                val color = ContextCompat.getColor(requireContext(), R.color.high_priority_color)
                cellItemLinearMain.setBackgroundColor(color)
            }
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}