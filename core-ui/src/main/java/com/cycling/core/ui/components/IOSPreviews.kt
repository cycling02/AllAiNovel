package com.cycling.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cycling.core.ui.theme.AllAiNovelTheme

@Preview(showBackground = true, name = "IOS Components - Light")
@Composable
fun IOSComponentsPreview() {
    AllAiNovelTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { PreviewSection("Buttons") }
                item { PreviewButtons() }
                item { PreviewSection("Cards & List Items") }
                item { PreviewCards() }
                item { PreviewSection("Input Fields") }
                item { PreviewInputs() }
                item { PreviewSection("Feedback") }
                item { PreviewFeedback() }
                item { PreviewSection("Badges & Icons") }
                item { PreviewBadges() }
            }
        }
    }
}

@Preview(showBackground = true, name = "IOS Components - Dark")
@Composable
fun IOSComponentsPreviewDark() {
    AllAiNovelTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { PreviewSection("Buttons") }
                item { PreviewButtons() }
                item { PreviewSection("Cards & List Items") }
                item { PreviewCards() }
                item { PreviewSection("Input Fields") }
                item { PreviewInputs() }
                item { PreviewSection("Feedback") }
                item { PreviewFeedback() }
                item { PreviewSection("Badges & Icons") }
                item { PreviewBadges() }
            }
        }
    }
}

@Composable
private fun PreviewSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun PreviewButtons() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IOSButton(
                text = "Primary",
                onClick = {},
                style = IOSButtonStyle.Primary,
                modifier = Modifier.weight(1f)
            )
            IOSButton(
                text = "Secondary",
                onClick = {},
                style = IOSButtonStyle.Secondary,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IOSButton(
                text = "Tertiary",
                onClick = {},
                style = IOSButtonStyle.Tertiary,
                modifier = Modifier.weight(1f)
            )
            IOSButton(
                text = "Error",
                onClick = {},
                style = IOSButtonStyle.Error,
                modifier = Modifier.weight(1f)
            )
        }
        IOSButton(
            text = "With Icon",
            onClick = {},
            icon = Icons.Default.Add
        )
        IOSButton(
            text = "Loading",
            onClick = {},
            loading = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IOSCompactButton("Small", onClick = {})
            IOSCompactButton("Icon", onClick = {}, icon = Icons.Default.Edit)
        }
    }
}

@Composable
private fun PreviewCards() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        IOSCard {
            Text("Basic Card", style = MaterialTheme.typography.bodyLarge)
            Text("Card content goes here", style = MaterialTheme.typography.bodySmall)
        }
        
        IOSListItemCard(
            icon = Icons.Outlined.Book,
            title = "Book Title",
            subtitle = "1,234 words",
            onClick = {}
        )
        
        IOSListItemCard(
            icon = Icons.Outlined.Favorite,
            iconBackground = MaterialTheme.colorScheme.error,
            title = "Favorites",
            badge = "12",
            onClick = {}
        )
        
        IOSSection(title = "List Section") {
            IOSListItem(
                icon = Icons.Outlined.Person,
                title = "Profile",
                showChevron = true
            )
            IOSListItem(
                icon = Icons.Default.Settings,
                iconBackground = MaterialTheme.colorScheme.secondary,
                title = "Settings",
                subtitle = "Customize your experience",
                showChevron = true
            )
            IOSListItem(
                title = "No Icon Item",
                value = "Value",
                showChevron = true,
                showDivider = false
            )
        }
    }
}

@Composable
private fun PreviewInputs() {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf("") }
    
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        IOSTextField(
            value = text1,
            onValueChange = { text1 = it },
            placeholder = "Enter text...",
            label = "Username"
        )
        
        IOSTextField(
            value = text2,
            onValueChange = { text2 = it },
            placeholder = "With icon",
            leadingIcon = Icons.Default.Search
        )
        
        IOSTextField(
            value = text3,
            onValueChange = { text3 = it },
            placeholder = "Error state",
            isError = true,
            errorMessage = "This field is required"
        )
        
        IOSPasswordTextField(
            value = "",
            onValueChange = {},
            placeholder = "Password"
        )
    }
}

@Composable
private fun PreviewFeedback() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.height(80.dp)) {
            IOSLoading(message = "Loading...")
        }
        
        IOSEmptyState(
            icon = Icons.Outlined.Book,
            title = "No Books",
            message = "Create your first book to get started",
            modifier = Modifier.height(200.dp),
            action = {
                IOSCompactButton("Create", onClick = {}, icon = Icons.Default.Add)
            }
        )
        
        IOSErrorState(
            message = "Something went wrong. Please try again.",
            modifier = Modifier.height(150.dp),
            onRetry = {}
        )
    }
}

@Composable
private fun PreviewBadges() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IOSBadge(text = "New")
            IOSBadge(
                text = "12",
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
            IOSBadge(
                text = "Error",
                backgroundColor = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.onError
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IOSIconBadge(
                icon = Icons.Default.Book,
                backgroundColor = MaterialTheme.colorScheme.primary
            )
            IOSIconBadge(
                icon = Icons.Default.Favorite,
                backgroundColor = MaterialTheme.colorScheme.error
            )
            IOSIconBadge(
                icon = Icons.Default.Settings,
                backgroundColor = MaterialTheme.colorScheme.secondary
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IOSFAB(onClick = {})
            IOSFAB(onClick = {}, text = "Create")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun IOSNavBarPreview() {
    AllAiNovelTheme {
        Column {
            IOSNavBar(
                title = "Page Title",
                onBack = {}
            )
            IOSLargeNavBar(
                title = "Large Title",
                onBack = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IOSSearchBarPreview() {
    AllAiNovelTheme {
        var query by remember { mutableStateOf("") }
        IOSSearchBar(
            query = query,
            onQueryChange = { query = it },
            placeholder = "Search books..."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IOSSwitchPreview() {
    AllAiNovelTheme {
        var checked by remember { mutableStateOf(true) }
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IOSSwitch(checked = false, onCheckedChange = {})
            IOSSwitch(checked = checked, onCheckedChange = { checked = it })
        }
    }
}
