package net.deechael.esjzone.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import net.deechael.esjzone.MainActivity
import net.deechael.esjzone.R
import net.deechael.esjzone.novellibrary.novel.CoveredNovel
import net.deechael.esjzone.novellibrary.novel.CoveredNovelImpl
import net.deechael.esjzone.ui.navigation.LocalBaseNavigator
import net.deechael.esjzone.ui.page.NovelPage

@Composable
fun Novel(covered: CoveredNovel, onClick: (() -> Unit)? = null) {
    val navigator = LocalBaseNavigator.current
    val configuration = LocalConfiguration.current

    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 4.dp)
            .fillMaxWidth()
            .clickable {
                if (onClick != null) {
                    onClick()
                } else {
                    navigator.push(NovelPage(covered))
                }
            }
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(covered.coverUrl)
                .crossfade(true)
                .build(),
            contentDescription = covered.name,
            imageLoader = MainActivity.imageLoader,
            loading = {
                CircularProgressIndicator()
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.empty_cover),
                    contentDescription = ""
                )
            },
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(8.dp)
                .height((configuration.screenHeightDp / 3.5).dp)
                .width((configuration.screenWidthDp / 2.5).dp)
        )
        Column {
            Text(
                text = covered.name,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 4.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.RemoveRedEye,
                    contentDescription = ""
                )
                Text(
                    text = "${covered.views}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 3.em,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp,
                        start = 8.dp,
                        end = 4.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = ""
                )
                Text(
                    text = "${covered.likes}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 3.em,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun NovelPreview() {
    Novel(
        covered = CoveredNovelImpl(
            "",
            "adadadadadadad",
            "",
            11,
            11,
            false,
        )
    )
}