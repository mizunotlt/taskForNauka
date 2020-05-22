package com.example.testnauka.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testnauka.data.ProdCalendarData
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.annotation.CalendarViewModelScope
import com.example.testnauka.di.module.RepositoryModule
import com.savvi.rangedatepicker.CalendarPickerView
import ru.cleverpumpkin.calendar.CalendarDate
import java.util.*
import com.savvi.rangedatepicker.SubTitle
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import com.example.testnauka.models.CalendarViewModel
import com.example.testnauka.R
import toothpick.ktp.delegate.inject



class CalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by inject()
    private lateinit var viewCalend: View
    private lateinit var calendarView: CalendarPickerView
    private lateinit var subTitle: ArrayList<SubTitle>
    private lateinit var initialDate: CalendarDate
    private lateinit var maxDate: CalendarDate

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private fun injectDependencies() {

        KTP.openScopes(ApplicationScope::class.java)
            .openSubScope(CalendarViewModelScope::class.java) { scope: Scope ->
                scope.installViewModelBinding<CalendarViewModel>(this)
                    .closeOnViewModelCleared(this)
                    .installModules(
                        RepositoryModule()
                    )
            }
            .closeOnDestroy(this)
            .inject(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewCalend =  inflater.inflate(R.layout.calendar_fragment, container, false)
        calendarView = viewCalend.findViewById(R.id.calendar_view)
        val calendar = Calendar.getInstance()
        subTitle = arrayListOf()
        calendarViewModel.getProdCalendar()
        calendar.set(2020, Calendar.JANUARY, 1)
        initialDate = CalendarDate(calendar.time)
        calendar.set(2020, Calendar.DECEMBER, 31)
        maxDate = CalendarDate(calendar.time)
        return viewCalend
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        calendarViewModel.calendarLiveData.observe(viewLifecycleOwner, Observer<List<ProdCalendarData>> {

            if (calendarViewModel.calendarLiveData.value != null){
                for (elem in calendarViewModel.calendarLiveData.value!!){
                    subTitle.add(SubTitle(elem.date, elem.dayType.toString()))
                }
                calendarView.init(initialDate.date,maxDate.date).inMode(CalendarPickerView.SelectionMode.SINGLE)
                    .withSubTitles(subTitle)
            }
        })

    }

}
