package com.cycling.feature.tools.namegenerator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cycling.domain.model.NameType
import com.cycling.feature.tools.namegenerator.NameGeneratorEffect
import com.cycling.feature.tools.namegenerator.NameGeneratorIntent
import com.cycling.feature.tools.namegenerator.NameGeneratorViewModel
import com.cycling.feature.tools.namegenerator.components.FactionNameOptions
import com.cycling.feature.tools.namegenerator.components.FavoriteNameList
import com.cycling.feature.tools.namegenerator.components.GeneratedNameCard
import com.cycling.feature.tools.namegenerator.components.NameTypeSelector
import com.cycling.feature.tools.namegenerator.components.PersonNameOptions
import com.cycling.feature.tools.namegenerator.components.PlaceNameOptions
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameGeneratorScreen(
    onNavigateBack: () -> Unit,
    viewModel: NameGeneratorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect = viewModel.effect

    LaunchedEffect(effect) {
        effect.collectLatest { effect ->
            when (effect) {
                is NameGeneratorEffect.ShowToast -> {
                }
                is NameGeneratorEffect.ShowError -> {
                }
                is NameGeneratorEffect.CopyToClipboard -> {
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("名字生成器") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onIntent(NameGeneratorIntent.ToggleFavorites) }) {
                        Icon(
                            imageVector = if (state.showFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "收藏"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.showFavorites) {
            FavoriteNameList(
                favorites = state.favorites,
                onDelete = { id ->
                    viewModel.onIntent(NameGeneratorIntent.RemoveFromFavorite(id))
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NameTypeSelector(
                    selectedType = state.selectedNameType,
                    onTypeSelected = { type ->
                        viewModel.onIntent(NameGeneratorIntent.SelectNameType(type))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                when (state.selectedNameType) {
                    NameType.PERSON_NAME -> {
                        PersonNameOptions(
                            gender = state.gender,
                            charCount = state.charCount,
                            onGenderSelected = { gender ->
                                viewModel.onIntent(NameGeneratorIntent.SelectGender(gender))
                            },
                            onCharCountSelected = { count ->
                                viewModel.onIntent(NameGeneratorIntent.SelectCharCount(count))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                    NameType.PLACE_NAME -> {
                        PlaceNameOptions(
                            placeType = state.placeType,
                            onPlaceTypeSelected = { type ->
                                viewModel.onIntent(NameGeneratorIntent.SelectPlaceType(type))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                    NameType.FACTION_NAME -> {
                        FactionNameOptions(
                            factionType = state.factionType,
                            onFactionTypeSelected = { type ->
                                viewModel.onIntent(NameGeneratorIntent.SelectFactionType(type))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "生成数量: ${state.generateCount}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Slider(
                        value = state.generateCount.toFloat(),
                        onValueChange = { count ->
                            viewModel.onIntent(NameGeneratorIntent.SetGenerateCount(count.toInt()))
                        },
                        valueRange = 1f..20f,
                        steps = 19,
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = { viewModel.onIntent(NameGeneratorIntent.GenerateNames) },
                    enabled = !state.isGenerating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (state.isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("生成中...")
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("生成名字")
                    }
                }

                if (state.generatedNames.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.generatedNames) { name ->
                            GeneratedNameCard(
                                name = name,
                                nameType = state.selectedNameType,
                                isFavorite = state.favorites.any {
                                    it.name == name && it.type == state.selectedNameType
                                },
                                onCopy = {
                                    viewModel.onIntent(NameGeneratorIntent.CopyName(name))
                                },
                                onFavorite = {
                                    viewModel.onIntent(
                                        NameGeneratorIntent.AddToFavorites(name, state.selectedNameType)
                                    )
                                }
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AutoFixHigh,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "点击生成按钮开始",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "支持生成人名、地名、势力名",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
