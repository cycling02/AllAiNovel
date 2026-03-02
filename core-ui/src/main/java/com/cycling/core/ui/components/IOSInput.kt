package com.cycling.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class IOSButtonStyle {
    Primary, Secondary, Tertiary, Error, Text
}

@Composable
fun IOSButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: IOSButtonStyle = IOSButtonStyle.Primary,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    loading: Boolean = false
) {
    val buttonModifier = modifier
        .fillMaxWidth()
        .height(IOSSize.touchTarget)
    
    when (style) {
        IOSButtonStyle.Primary -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !loading,
                shape = RoundedCornerShape(IOSRadius.lg)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    if (icon != null) {
                        Icon(icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(IOSSpacing.sm))
                    }
                    Text(text)
                }
            }
        }
        IOSButtonStyle.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !loading,
                shape = RoundedCornerShape(IOSRadius.lg)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    if (icon != null) {
                        Icon(icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(IOSSpacing.sm))
                    }
                    Text(text)
                }
            }
        }
        IOSButtonStyle.Tertiary -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !loading,
                shape = RoundedCornerShape(IOSRadius.lg)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                } else {
                    if (icon != null) {
                        Icon(icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(IOSSpacing.sm))
                    }
                    Text(text)
                }
            }
        }
        IOSButtonStyle.Error -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !loading,
                shape = RoundedCornerShape(IOSRadius.lg),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    if (icon != null) {
                        Icon(icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(IOSSpacing.sm))
                    }
                    Text(text)
                }
            }
        }
        IOSButtonStyle.Text -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                enabled = enabled && !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    if (icon != null) {
                        Icon(icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(IOSSpacing.sm))
                    }
                    Text(text)
                }
            }
        }
    }
}

@Composable
fun IOSCompactButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: IOSButtonStyle = IOSButtonStyle.Primary,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    when (style) {
        IOSButtonStyle.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = RoundedCornerShape(IOSRadius.md)
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(IOSSize.iconSm))
                    Spacer(modifier = Modifier.width(IOSSpacing.xs))
                }
                Text(text)
            }
        }
        IOSButtonStyle.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = RoundedCornerShape(IOSRadius.md)
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(IOSSize.iconSm))
                    Spacer(modifier = Modifier.width(IOSSpacing.xs))
                }
                Text(text)
            }
        }
        IOSButtonStyle.Tertiary -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = RoundedCornerShape(IOSRadius.md)
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(IOSSize.iconSm))
                    Spacer(modifier = Modifier.width(IOSSpacing.xs))
                }
                Text(text)
            }
        }
        IOSButtonStyle.Text -> {
            TextButton(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(IOSSize.iconSm))
                    Spacer(modifier = Modifier.width(IOSSpacing.xs))
                }
                Text(text)
            }
        }
        IOSButtonStyle.Error -> {
            Button(
                onClick = onClick,
                modifier = modifier,
                enabled = enabled,
                shape = RoundedCornerShape(IOSRadius.md),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(IOSSize.iconSm))
                    Spacer(modifier = Modifier.width(IOSSpacing.xs))
                }
                Text(text)
            }
        }
    }
}

@Composable
fun IOSTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = IOSSpacing.xs)
            )
        }
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            leadingIcon = if (leadingIcon != null) {
                { Icon(leadingIcon, contentDescription = null) }
            } else null,
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.Sentences
            ),
            enabled = enabled,
            readOnly = readOnly,
            shape = RoundedCornerShape(IOSRadius.lg),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
        
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = IOSSpacing.xs)
            )
        }
    }
}

@Composable
fun IOSPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "密码",
    label: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    IOSTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        leadingIcon = Icons.Default.Lock,
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                )
            }
        },
        isError = isError,
        errorMessage = errorMessage,
        keyboardType = KeyboardType.Password,
        enabled = enabled,
        singleLine = true
    )
}

@Composable
fun IOSMultilineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String? = null,
    minLines: Int = 3,
    maxLines: Int = 5,
    enabled: Boolean = true
) {
    IOSTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        label = label,
        singleLine = false,
        maxLines = maxLines,
        enabled = enabled
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOSBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    confirmText: String? = "确定",
    dismissText: String? = "取消",
    confirmEnabled: Boolean = true,
    onConfirm: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = IOSRadius.xl, topEnd = IOSRadius.xl),
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = IOSSpacing.sm)
                        .size(36.dp, 5.dp)
                        .clip(RoundedCornerShape(IOSRadius.full))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IOSSpacing.lg)
                    .padding(bottom = IOSSpacing.xxl)
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = IOSSpacing.md)
                    )
                }
                
                content()
                
                if (onConfirm != null) {
                    Spacer(modifier = Modifier.height(IOSSpacing.lg))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(IOSSpacing.md)
                    ) {
                        if (dismissText != null) {
                            IOSButton(
                                text = dismissText,
                                onClick = onDismiss,
                                style = IOSButtonStyle.Secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        IOSButton(
                            text = confirmText ?: "确定",
                            onClick = {
                                onConfirm()
                                onDismiss()
                            },
                            enabled = confirmEnabled,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IOSInputDialog(
    visible: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    initialValue: String = "",
    confirmText: String = "确定",
    dismissText: String = "取消",
    label: String? = null,
    singleLine: Boolean = true
) {
    var value by remember(initialValue) { mutableStateOf(initialValue) }
    
    IOSBottomSheet(
        visible = visible,
        onDismiss = {
            value = initialValue
            onDismiss()
        },
        title = title,
        confirmText = confirmText,
        dismissText = dismissText,
        confirmEnabled = value.isNotBlank(),
        onConfirm = {
            onConfirm(value)
            value = initialValue
        }
    ) {
        IOSTextField(
            value = value,
            onValueChange = { value = it },
            placeholder = placeholder,
            label = label,
            singleLine = singleLine
        )
    }
}
