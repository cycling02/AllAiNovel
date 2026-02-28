package com.cycling.feature.book.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cycling.domain.model.Book
import com.cycling.domain.model.BookStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getStatusText(book.status),
                        style = MaterialTheme.typography.bodySmall,
                        color = getStatusColor(book.status)
                    )
                    if (book.wordCount > 0) {
                        Text(
                            text = " · ${book.wordCount}字",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                if (book.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun getStatusText(status: BookStatus): String {
    return when (status) {
        BookStatus.DRAFT -> "草稿"
        BookStatus.ONGOING -> "连载中"
        BookStatus.COMPLETED -> "已完结"
        BookStatus.PAUSED -> "暂停"
    }
}

@Composable
fun getStatusColor(status: BookStatus): androidx.compose.ui.graphics.Color {
    return when (status) {
        BookStatus.DRAFT -> androidx.compose.ui.graphics.Color.Gray
        BookStatus.ONGOING -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        BookStatus.COMPLETED -> androidx.compose.ui.graphics.Color(0xFF2196F3)
        BookStatus.PAUSED -> androidx.compose.ui.graphics.Color(0xFFFF9800)
    }
}