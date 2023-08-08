package me.geoffery.kentric.di

import androidx.work.WorkManager
import me.geoffery.kentric.ui.GeneratorScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val modules = module {
    viewModel {
        GeneratorScreenViewModel()
    }

    // workers

    single {
        WorkManager.getInstance(get())
    }
}