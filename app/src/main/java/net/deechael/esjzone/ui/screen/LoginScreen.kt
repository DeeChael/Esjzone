package net.deechael.esjzone.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.network.EsjzoneClient
import net.deechael.esjzone.network.features.login

object LoginScreen : Screen {

    private fun readResolve(): Any = LoginScreen

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 12.em
            )

            Spacer(modifier = Modifier.height(50.dp))

            var email by remember {
                mutableStateOf("")
            }
            var password by remember {
                mutableStateOf("")
            }
            var passwordVisible by remember {
                mutableStateOf(false)
            }
            var buttonEnabled by remember {
                mutableStateOf(true)
            }
            var loggingIn by remember {
                mutableStateOf(false)
            }
            
            val scope = rememberCoroutineScope()

            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                        Text(text = stringResource(id = R.string.email))
                },
                placeholder = {
                        Text(text = stringResource(id = R.string.email))
                },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                        Text(text = stringResource(id = R.string.password))
                },
                placeholder = {
                        Text(text = stringResource(id = R.string.password))
                },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = stringResource(id = if (passwordVisible) R.string.password_hide else R.string.password_show)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                buttonEnabled = false
                loggingIn = true
                scope.launch(Dispatchers.IO) {
                    val authorization = EsjzoneClient.login(email, password)
                    if (authorization != null) {
                        val ewsKeyCache = MainActivity.database.cacheDao().findByKey("ews_key")
                        val ewsTokenCache = MainActivity.database.cacheDao().findByKey("ews_token")

                        ewsKeyCache.value = authorization.ewsKey
                        ewsTokenCache.value = authorization.ewsToken

                        MainActivity.database.cacheDao().update(ewsKeyCache, ewsTokenCache)

                        launch(Dispatchers.Main) {
                            Toast.makeText(context, R.string.login_success, Toast.LENGTH_SHORT).show()
                            navigator.replace(MainScreen(authorization = authorization))
                        }
                    } else {
                        launch(Dispatchers.Main) {
                            Toast.makeText(context, R.string.login_fail, Toast.LENGTH_SHORT).show()
                        }
                        loggingIn = false
                        buttonEnabled = true
                    }
                }
            },
                enabled = buttonEnabled
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.button_login)
                    )

                    if (!buttonEnabled) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}