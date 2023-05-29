package com.topic2.android.notes.ui.components.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.topic2.android.notes.domain.model.NoteModel
import com.topic2.android.notes.routing.Screen
import com.topic2.android.notes.ui.components.AppDrawer
import com.topic2.android.notes.ui.components.Note
import com.topic2.android.notes.ui.components.TopAppBar
import com.topic2.android.notes.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    viewModel: MainViewModel
) {
    val notes: List<NoteModel> by viewModel
        .notesNotInTrash
        .observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar =  {
        TopAppBar(
            title = "Notes",
            icon = Icons.Filled.List,
            onIconClick = {
                coroutineScope.launch {
                    scaffoldState.drawerState.open()
                }
            }
        )
    },
        scaffoldState = scaffoldState,
        drawerContent = {
             AppDrawer(
                 currentScreen = Screen.Notes, closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                 }
             )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewNoteClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                   Icon(
                       imageVector = Icons.Filled.Add,
                       contentDescription = "Add Note Button"
                   )
                }
            )
        },
        content = {
            if (notes.isNotEmpty()){
                NotesList(
                    notes = notes, onNoteCheckedChange = {
                      viewModel.onNoteCheckedChange(it)
                    },
                    onNoteClick = {viewModel.onNoteClick(it)}
                )
            }
        }
    )
}

@Composable
private fun NotesList(
    notes: List<NoteModel>,
    onNoteCheckedChange: (NoteModel) -> Unit,
    onNoteClick: (NoteModel) -> Unit
) {
    LazyColumn{
        items(count = notes.size) {noteIndex ->
            val note = notes[noteIndex]
            Note(
                note = note,
                onNoteClick = onNoteClick,
                onNoteCheckedChange = onNoteCheckedChange
            )
        }
    }
}

@Preview
@Composable
private fun NotesListPreview() {
    NotesList(
        notes = listOf(
            NoteModel(id = 1, title = "Note 1", content = "Content 1", isCheckedOff = null),
            NoteModel(id = 2, title = "Note 2", content = "Content 2", isCheckedOff = false),
            NoteModel(id = 3, title = "Note 3", content = "Content 3", isCheckedOff = true)
        ),
        onNoteCheckedChange = {},
        onNoteClick = {}
    )
}