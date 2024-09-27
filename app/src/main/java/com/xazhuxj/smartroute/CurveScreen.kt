package com.xazhuxj.smartroute

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.xazhuxj.smartroute.models.CircleCurve
import com.xazhuxj.smartroute.models.GPoint
import com.xazhuxj.smartroute.models.TransitionCurve
import kotlinx.coroutines.launch
import org.apache.commons.lang3.math.NumberUtils


@Composable
fun CurveScreen(onNavigateToResultScreen: () -> Unit, modifier: Modifier = Modifier) {
    val dirJdStartAlpha = remember { mutableStateOf(true) } //交点-起点-方向角 or 交点-起点-终点

    val kJD = remember { mutableDoubleStateOf(5330.198) }
    val xJD = remember { mutableDoubleStateOf(3088386.436) }
    val yJD = remember { mutableDoubleStateOf(66798.566) }

    val xStart = remember { mutableDoubleStateOf(3088256.238) }
    val yStart = remember { mutableDoubleStateOf(66798.566) }

    val xEnd = remember { mutableDoubleStateOf(3088514.534) }
    val yEnd = remember { mutableDoubleStateOf(66821.858) }
    val alpha = remember { mutableDoubleStateOf(10.182) }
    val r0 = remember { mutableDoubleStateOf(1000.0) }
    val l0 = remember { mutableDoubleStateOf(80.0) }

    val kNo = remember { mutableDoubleStateOf(5359.866) }
    val length = remember { mutableDoubleStateOf(20.0) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "已知数据输入方式",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .selectableGroup()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = dirJdStartAlpha.value,
                    onClick = { dirJdStartAlpha.value = true },
                    modifier = Modifier.semantics { contentDescription = "交点-起点-偏转角" }
                )
                Text(
                    text = "交点-起点-偏转角",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !dirJdStartAlpha.value,
                    onClick = { dirJdStartAlpha.value = false },
                    modifier = Modifier.semantics { contentDescription = "交点-起点-终点" }
                )
                Text(
                    text = "交点-起点-终点",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Text(
            text = "曲线交点(JD)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        DoubleInputText(kJD, "公里桩号",  modifier = Modifier.fillMaxWidth())

        Row {

            DoubleInputText(xJD, "X(N)",  modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.width(5.dp))

            DoubleInputText(yJD, "Y(E)",  modifier = Modifier.weight(1f))
        }

        Text(
            "起点(ZY/ZH)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Row {
            DoubleInputText(xStart, "X(N)",  modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(5.dp))
            DoubleInputText(yStart, "Y(E)",  modifier = Modifier.weight(1f))
        }

        Text(
            "终点(YZ/HZ)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DoubleInputText(xEnd, "X(N)",  enabled = !dirJdStartAlpha.value, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(5.dp))
            DoubleInputText(yEnd, "Y(E)",  enabled = !dirJdStartAlpha.value, modifier = Modifier.weight(1f))
        }

        Text(
            "曲线参数",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        DoubleInputText(alpha, "偏转角",  enabled = dirJdStartAlpha.value, modifier = Modifier.fillMaxWidth())
        DoubleInputText(r0, "圆曲线半径",  modifier = Modifier.fillMaxWidth())
        DoubleInputText(l0, "缓和曲线长", modifier = Modifier.fillMaxWidth())

        Text(
            "任意曲线点坐标计算",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Row {
            DoubleInputText(kNo, "桩号", modifier = Modifier.fillMaxWidth().weight(1f))
            Button(
                onClick = onNavigateToResultScreen
//                onClick = {
//                    coroutineScope.launch {//放入协程中进行计算
//                        if (l0.doubleValue <= 0.0) //圆曲线
//                        {
//                            val r = if (dirJdStartAlpha.value)
//                                CircleCurve(
//                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
//                                    GPoint(
//                                        kNo = kJD.doubleValue,
//                                        x = xJD.doubleValue,
//                                        y = yJD.doubleValue
//                                    ),
//                                    radius = r0.doubleValue,
//                                    alpha = alpha.doubleValue
//                                )
//                            else
//                                CircleCurve(
//                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
//                                    GPoint(
//                                        kNo = kJD.doubleValue,
//                                        x = xJD.doubleValue,
//                                        y = yJD.doubleValue
//                                    ),
//                                    GPoint(x = xEnd.doubleValue, y = yEnd.doubleValue),
//                                    radius = r0.doubleValue
//                                )
//
//                            val pt = r.calPointOnCurveByKno(kNo.doubleValue)
//                            Log.d("TTT", "CurveScreen: ${r}")
//                            Log.d("TTT", "CurveScreen: ${pt}")
//
//                        } else { //缓和曲线
//                            val r = if (dirJdStartAlpha.value)
//                                TransitionCurve(
//                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
//                                    GPoint(
//                                        kNo = kJD.doubleValue,
//                                        x = xJD.doubleValue,
//                                        y = yJD.doubleValue
//                                    ),
//                                    radius = r0.doubleValue,
//                                    alpha = alpha.doubleValue,
//                                    l0 = l0.doubleValue
//                                )
//                            else
//                                TransitionCurve(
//                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
//                                    GPoint(
//                                        kNo = kJD.doubleValue,
//                                        x = xJD.doubleValue,
//                                        y = yJD.doubleValue
//                                    ),
//                                    GPoint(x = xEnd.doubleValue, y = yEnd.doubleValue),
//                                    radius = r0.doubleValue,
//                                    l0 = l0.doubleValue
//                                )
//
//                            val pt = r.calPointOnCurveByKno(kNo.doubleValue)
//                            Log.d("TTT", "CurveScreen: ${r}")
//                            Log.d("TTT", "CurveScreen: ${pt}")
//                        }
//                    }
//                },
            ) {
                Text(text = "单个曲线点计算") //单点坐标计算，不需要线路长度，可以设为0，默认为20
            }
        }

        Text(
            "批量曲线点坐标计算",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(modifier = Modifier.weight(1f)) {
                DoubleInputText(length, "里程间距")
            }

            Button(
                onClick = {
                    coroutineScope.launch {//放入协程中进行计算
                        if (l0.doubleValue <= 0.0) //圆曲线
                        {
                            val r = if (dirJdStartAlpha.value)
                                CircleCurve(
                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
                                    GPoint(
                                        kNo = kJD.doubleValue,
                                        x = xJD.doubleValue,
                                        y = yJD.doubleValue
                                    ),
                                    radius = r0.doubleValue,
                                    alpha = alpha.doubleValue
                                )
                            else
                                CircleCurve(
                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
                                    GPoint(
                                        kNo = kJD.doubleValue,
                                        x = xJD.doubleValue,
                                        y = yJD.doubleValue
                                    ),
                                    GPoint(x = xEnd.doubleValue, y = yEnd.doubleValue),
                                    radius = r0.doubleValue
                                )

                            val pt = r.calPointOnCurveByKno(kNo.doubleValue)
                            Log.d("TTT", "CurveScreen: $r")
                            Log.d("TTT", "CurveScreen: $pt")

                        } else { //缓和曲线
                            val r = if (dirJdStartAlpha.value)
                                TransitionCurve(
                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
                                    GPoint(
                                        kNo = kJD.doubleValue,
                                        x = xJD.doubleValue,
                                        y = yJD.doubleValue
                                    ),
                                    radius = r0.doubleValue,
                                    alpha = alpha.doubleValue,
                                    l0 = l0.doubleValue
                                )
                            else
                                TransitionCurve(
                                    GPoint(x = xStart.doubleValue, y = yStart.doubleValue),
                                    GPoint(
                                        kNo = kJD.doubleValue,
                                        x = xJD.doubleValue,
                                        y = yJD.doubleValue
                                    ),
                                    GPoint(x = xEnd.doubleValue, y = yEnd.doubleValue),
                                    radius = r0.doubleValue,
                                    l0 = l0.doubleValue
                                )

                            val pts = r.calAllPoints(length.doubleValue)
                            Log.d("TTT", "CurveScreen: $r")
                            Log.d("TTT", "CurveScreen: $pts")
                        }
                    }
                },
            ) {
                Text(text = "整体要素计算")
            }
        }
    }
}


//输入框
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DoubleInputText(
    doubleValue: MutableDoubleState,
    labelText: String,
    modifier: Modifier = Modifier,
    enabled:Boolean = true,
    onImeAction: () -> Unit = { }  //配置软键盘
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = doubleValue.doubleValue.toString(),
        onValueChange = {
            if (NumberUtils.isCreatable(it))
                doubleValue.doubleValue = NumberUtils.toDouble(it)
        },
        label = {
            Text(labelText)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        maxLines = 1,
        enabled = enabled,
        modifier = modifier,
        //配置软键盘
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType= KeyboardType.Decimal),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        })
    )
}
