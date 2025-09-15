package com.gado.madarsofttask.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gado.madarsofttask.presentation.adduser.AddUserScreen
import com.gado.madarsofttask.presentation.userlist.UserListScreen

object Screen {
    const val USER_LIST = "user_list"
    const val ADD_USER = "add_user"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.USER_LIST
    ) {
        composable(Screen.USER_LIST) {
            UserListScreen(
                onAddUserClick = {
                    navController.navigate(Screen.ADD_USER)
                }
            )
        }
        
        composable(Screen.ADD_USER) {
            AddUserScreen(
                onUserSaved = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}