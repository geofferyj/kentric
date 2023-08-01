package me.geoffery.kentric.ui.main

import androidx.work.WorkManager
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