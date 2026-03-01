package com.cycling.feature.tools.namegenerator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cycling.domain.usecase.namegenerator.Gender

@Composable
fun PersonNameOptions(
    gender: Gender,
    charCount: Int,
    onGenderSelected: (Gender) -> Unit,
    onCharCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "性别",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = gender == Gender.MALE,
                onClick = { onGenderSelected(Gender.MALE) },
                label = { Text("男") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = gender == Gender.FEMALE,
                onClick = { onGenderSelected(Gender.FEMALE) },
                label = { Text("女") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = gender == Gender.RANDOM,
                onClick = { onGenderSelected(Gender.RANDOM) },
                label = { Text("随机") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "名字字数",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = charCount == 1,
                onClick = { onCharCountSelected(1) },
                label = { Text("单字名") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = charCount == 2,
                onClick = { onCharCountSelected(2) },
                label = { Text("双字名") }
            )
        }
    }
}
