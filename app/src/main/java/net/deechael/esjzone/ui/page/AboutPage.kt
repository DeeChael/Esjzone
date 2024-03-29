package net.deechael.esjzone.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import net.deechael.esjzone.Constants
import net.deechael.esjzone.R
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator

object AboutPage : Screen {

    private fun readResolve(): Any = AboutPage

    @Composable
    override fun Content() {
        val navigator = LocalBaseNavigator.current
        val uriHandler = LocalUriHandler.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = {
                            navigator.pop()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, bottom = 4.dp)
                            .size(50.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBackIosNew, contentDescription = "")
                    }
                    Spacer(modifier = Modifier.weight(3f))
                    Text(
                        text = stringResource(id = R.string.about),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(
                            top = 24.dp,
                            bottom = 4.dp
                        )
                    )
                    Spacer(modifier = Modifier.weight(5f))
                }
                HorizontalDivider(thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.maintainers),
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            for (maintainer in Constants.MAINTAINERS) {
                Text(
                    text = maintainer,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.contributors),
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            for (contributor in Constants.CONTRIBUTORS) {
                Text(
                    text = contributor,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.open_source_libraries),
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            for (library in Constants.OPEN_SOURCE_LIBRARIES) {
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(start = 32.dp, end = 32.dp))
                Row(
                    Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 4.dp)
                        .fillMaxWidth()
                        .clickable {
                            uriHandler.openUri(library.url)
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = library.name, modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = library.owner, modifier = Modifier.padding(end = 8.dp, top = 4.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = library.description, modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Powered by ")
                Image(
                    painter = painterResource(id = R.drawable.jetpack_compose_high),
                    contentScale = ContentScale.Inside,
                    contentDescription = "jetpack compose",
                    modifier = Modifier.height(20.dp)
                )
                Text(text = "Jetpack Compose", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}