package net.deechael.esjzone.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.deechael.esjzone.R
import net.deechael.esjzone.novellibrary.component.ChapterItem
import net.deechael.esjzone.novellibrary.component.ChapterListItem
import net.deechael.esjzone.novellibrary.component.TextComponent
import net.deechael.esjzone.novellibrary.component.TextItem
import net.deechael.esjzone.novellibrary.novel.Chapter
import net.deechael.esjzone.novellibrary.novel.NovelChapterList

@Composable
fun ChapterList(chapterList: NovelChapterList, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.novel_chapterlist),
            fontSize = 20.sp,
            modifier = Modifier.padding(12.dp)
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            for (item in chapterList.items) {
                item.Render()
            }
        }
    }
}

@Preview
@Composable
fun ChapterListPreview() {
    ChapterList(
        chapterList = NovelChapterList(
            listOf(
                TextItem(TextComponent("Just example")),
                ChapterItem(Chapter("example chapter 1", "")),
                ChapterItem(Chapter("example chapter 2", "")),
                ChapterListItem(
                    "Chapter list example",
                    listOf(
                        Chapter("in chapter list 1", ""),
                        Chapter("in chapter list 2", ""),
                        Chapter("in chapter list 3", ""),
                    )
                )
            )
        )
    )
}