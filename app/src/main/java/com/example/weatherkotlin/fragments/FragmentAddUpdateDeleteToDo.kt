package com.example.weatherkotlin.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weatherkotlin.BaseApplication
import com.example.weatherkotlin.R
import com.example.weatherkotlin.const.DATE_FORMAT_PATTERN_1
import com.example.weatherkotlin.databinding.FragmentAddUpdateDeleteToDoBinding
import com.example.weatherkotlin.receiver.AlarmReceiver
import com.example.weatherkotlin.util.calculateCurrentTimeMilliseconds
import com.example.weatherkotlin.util.calculateMillisecondsFromDate
import com.example.weatherkotlin.viewmodels.ToDoViewModel
import com.example.weatherkotlin.viewmodels.ToDoViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddUpdateDeleteToDo : Fragment() {

    private val navigationArgs: FragmentAddUpdateDeleteToDoArgs by navArgs()

    private val viewModel: ToDoViewModel by activityViewModels {
        ToDoViewModelFactory(
            activity?.application as BaseApplication
        )
    }

    private var _binding: FragmentAddUpdateDeleteToDoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUpdateDeleteToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            buttonSelectDeadlineDate.setOnClickListener {
                configDateButton(buttonSelectDeadlineDate.text.toString(), deadlineDate)
            }
            buttonSelectDeadlineHour.setOnClickListener {
                configTimeButton(buttonSelectDeadlineHour.text.toString(), deadlineHour)
            }
            alarmSwitch.setOnCheckedChangeListener { _, isChecked ->
                performDateCheck(
                    requireContext(),
                    binding.root,
                    alarmSwitch,
                    deadlineDate,
                    deadlineHour,
                    isChecked
                )
            }
        }

        val type = navigationArgs.crudType

        if (type == 0) {
            binding.apply {
                saveAction.setOnClickListener {
                    saveNewTask()
                }
                priorityUrgentImportant.isChecked = true
                deleteAction.visibility = View.GONE
            }
        } else {
            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                resources.getString(R.string.label_fragment_update_delete_to_do)
            applyBindingsToSelectedTask()
            binding.apply {
                saveAction.setOnClickListener {
                    updateTask()
                }
                deleteAction.visibility = View.VISIBLE
                deleteAction.setOnClickListener {
                    deleteTask()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveNewTask() {
        if (isEntryValid()) {
            binding.let {
                if (!it.alarmSwitch.isChecked) {
                    viewModel.addNewToDoItem(
                        it.taskName.text.toString().trim(),
                        it.taskDescription.text.toString().trim(),
                        getPriority(),
                        it.deadlineDate.text.toString(),
                        it.deadlineHour.text.toString(),
                        it.alarmSwitch.isChecked,
                        it.completedSwitch.isChecked
                    )
                } else {
                    viewModel.addNewToDoItemWithAlarm(
                        it.taskName.text.toString().trim(),
                        it.taskDescription.text.toString().trim(),
                        getPriority(),
                        it.deadlineDate.text.toString(),
                        it.deadlineHour.text.toString(),
                        it.alarmSwitch.isChecked,
                        it.completedSwitch.isChecked
                    )
                    sendAlarm()
                }
            }
            val action =
                FragmentAddUpdateDeleteToDoDirections.actionFragmentAddToDoToFragmentListToDo(
                    snackBarType = 1
                )
            findNavController().navigate(action)

        } else {
            Snackbar.make(
                requireContext(),
                binding.root,
                "Dữ liệu nhập vào không hợp lệ!",
                Snackbar.LENGTH_SHORT
            )
                .setAnchorView(R.id.bottom_nav)
                .show()
        }
    }

    private fun updateTask() {
        binding.let {
            if (isEntryValid()) {
                /*
                Nếu AlarmSwitch bật, task cũ đã bật alarm, thời gian mới khác thời gian cũ -> Hủy alarm cũ và Set alarm mới
                 */
                if (it.alarmSwitch.isChecked
                    && viewModel.selectedTask.value!!.isNotified
                    && (!it.deadlineDate.text.toString()
                        .equals(viewModel.selectedTask.value!!.deadlineDate)
                            || !it.deadlineHour.text.toString()
                        .equals(viewModel.selectedTask.value!!.deadlineHour))

                ) {
                    cancelAlarm(viewModel.selectedTask.value!!.id)
                    sendAlarmWithId(viewModel.selectedTask.value!!.id)
                }

                /*
                Nếu AlarmSwitch bật, task cũ chưa bật alarm -> Set alarm
                 */
                if (it.alarmSwitch.isChecked
                    && !viewModel.selectedTask.value!!.isNotified
                ) {
                    sendAlarmWithId(viewModel.selectedTask.value!!.id)
                }

                /*
                Nếu AlarmSwitch tắt, task cũ đã bật alarm -> Hủy alarm
                 */
                if (!it.alarmSwitch.isChecked && viewModel.selectedTask.value!!.isNotified) {
                    cancelAlarm(viewModel.selectedTask.value!!.id)
                }

                /*
                Update và điều hướng chung cho các tình huống
                 */
                viewModel.updateToDoItem(
                    viewModel.selectedTask.value!!.id,
                    it.taskName.text.toString().trim(),
                    it.taskDescription.text.toString().trim(),
                    getPriority(),
                    it.deadlineDate.text.toString(),
                    it.deadlineHour.text.toString(),
                    it.alarmSwitch.isChecked,
                    it.completedSwitch.isChecked
                )
                val action =
                    FragmentAddUpdateDeleteToDoDirections.actionFragmentAddToDoToFragmentListToDo(
                        snackBarType = 1
                    )
                findNavController().navigate(action)
            } else {
                Snackbar.make(
                    requireContext(),
                    it.root,
                    "Dữ liệu nhập vào không hợp lệ!",
                    Snackbar.LENGTH_SHORT
                )
                    .setAnchorView(R.id.bottom_nav)
                    .show()
            }
        }

    }

    private fun deleteTask() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Thông báo")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Bạn có chắc chắn muốn xóa nhắc việc này?")
            .setPositiveButton("Có") { _, _ ->
                cancelAlarm(viewModel.selectedTask.value!!.id)
                viewModel.deleteToDoItem(viewModel.selectedTask.value!!)
                val action =
                    FragmentAddUpdateDeleteToDoDirections.actionFragmentAddToDoToFragmentListToDo(
                        snackBarType = 2
                    )
                findNavController().navigate(action)
            }
            .setNegativeButton("Không") { _, _ -> }
            .create()
        alertDialogBuilder.show()
    }

    /*
    Gửi alarm khi tạo mới 1 task
     */
    private fun sendAlarm() {
        viewModel.insertedId.observe(viewLifecycleOwner) {
            if (it != null) {
                val notifyPendingIntent = createNotifyPendingIntent(it)

                val alarmManager =
                    requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                AlarmManagerCompat.setExact(
                    alarmManager,
                    AlarmManager.RTC,
                    calculateMillisecondsFromDate(
                        binding.deadlineDate.text.toString(),
                        binding.deadlineHour.text.toString()
                    ),
                    notifyPendingIntent
                )
                return@observe
            }
        }
    }

    /*
    Gửi alarm khi update 1 task
     */
    private fun sendAlarmWithId(id: Int) {
        val notifyPendingIntent = createNotifyPendingIntent(id)

        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmManagerCompat.setExact(
            alarmManager,
            AlarmManager.RTC,
            calculateMillisecondsFromDate(
                binding.deadlineDate.text.toString(),
                binding.deadlineHour.text.toString()
            ),
            notifyPendingIntent
        )
    }

    private fun createNotifyPendingIntent(insertedId: Int): PendingIntent {
        val notifyIntent = Intent(requireActivity(), AlarmReceiver::class.java)

        notifyIntent.let {
            it.putExtra("notiId", insertedId)
            it.putExtra("notiChannel", BaseApplication.CHANNEL_TASK_ID)
            it.putExtra("title", "Nhắc việc")
            it.putExtra(
                "body",
                "Đã đến hạn thực hiện công việc ".plus(binding.taskName.text.toString())
            )
        }

        // Support Android 12+ and compatible with lower versions
        val flags = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else -> PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getBroadcast(
            requireContext(),
            insertedId,
            notifyIntent,
            flags
        )
    }

    private fun cancelAlarm(id: Int) {
        val notifyPendingIntent = createNotifyPendingIntent(id)
        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(notifyPendingIntent)
        notifyPendingIntent.cancel()
    }

    private fun isEntryValid(): Boolean {
        binding.let {
            return viewModel.isEntryValid(
                it.taskName.text.toString(),
                it.taskDescription.text.toString(),
                getPriority(),
                it.deadlineDate.text.toString(),
                it.deadlineHour.text.toString(),
            )
        }
    }

    private fun getPriority(): Int {
        return binding.let {
            when {
                it.priorityUrgentImportant.isChecked -> 1
                it.priorityNotUrgentImportant.isChecked -> 2
                it.priorityUrgentNotImportant.isChecked -> 3
                else -> 4
            }
        }
    }

    private fun applyBindingsToSelectedTask() {
        binding.apply {
            viewModel.selectedTask.observe(viewLifecycleOwner) {
                taskName.setText(it.name)
                taskDescription.setText(it.description)
                when (it.priority) {
                    1 -> priorityUrgentImportant.isChecked = true
                    2 -> priorityNotUrgentImportant.isChecked = true
                    3 -> priorityUrgentNotImportant.isChecked = true
                    else -> priorityNotUrgentNotImportant.isChecked = true
                }
                deadlineDate.text = it.deadlineDate
                deadlineHour.text = it.deadlineHour
                alarmSwitch.isChecked = it.isNotified
                completedSwitch.isChecked = it.completed
            }
        }
    }

    private fun configDateButton(title: String, textView: TextView) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(title)
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            val selectedDate =
                SimpleDateFormat(DATE_FORMAT_PATTERN_1, Locale.getDefault()).format(calendar.time)
            textView.text = selectedDate
            performDateCheckWhenChanges()
        }

        datePicker.show(childFragmentManager, "date_picker")
    }

    private fun configTimeButton(title: String, textView: TextView) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
            .setTitleText(title)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hourOfDay = timePicker.hour
            val minute = timePicker.minute
            val formattedTime = when {
                (hourOfDay < 10) -> {
                    if (minute > 10) {
                        "0$hourOfDay:$minute"
                    } else {
                        "0$hourOfDay:0$minute"
                    }
                }
                else -> {
                    if (minute > 10) {
                        "$hourOfDay:$minute"
                    } else {
                        "$hourOfDay:0$minute"
                    }
                }
            }
            textView.text = formattedTime
            performDateCheckWhenChanges()
        }

        timePicker.show(childFragmentManager, "time_picker")
    }

    private fun performDateCheck(
        ctx: Context,
        view: View,
        switch: SwitchMaterial,
        date: MaterialTextView,
        hour: MaterialTextView,
        isChecked: Boolean
    ) {
        if (isChecked && date.text.isNotEmpty() && hour.text.isNotEmpty()) {
            if (calculateMillisecondsFromDate(
                    date.text.toString(),
                    hour.text.toString()
                ) <= calculateCurrentTimeMilliseconds()
            ) {
                Snackbar.make(
                    ctx,
                    view,
                    "Không thể đặt thông báo cho thời điểm trước hiện tại.",
                    Snackbar.LENGTH_SHORT
                )
                    .setAnchorView(R.id.bottom_nav)
                    .show()
                switch.isChecked = false
            }
        }
        if (isChecked && (date.text.isEmpty() || hour.text.isEmpty())) {
            Snackbar.make(
                ctx,
                view,
                "Vui lòng chọn ngày giờ để đặt chuông báo.",
                Snackbar.LENGTH_SHORT
            )
                .setAnchorView(R.id.bottom_nav)
                .show()
            switch.isChecked = false
        }
    }

    private fun performDateCheckWhenChanges() {
        binding.let {
            if (it.alarmSwitch.isChecked) {
                performDateCheck(
                    requireContext(),
                    binding.root,
                    it.alarmSwitch,
                    it.deadlineDate,
                    it.deadlineHour,
                    it.alarmSwitch.isChecked
                )
            }
        }
    }
}