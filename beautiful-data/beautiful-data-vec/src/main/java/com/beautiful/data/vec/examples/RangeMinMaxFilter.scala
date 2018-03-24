package com.beautiful.data.vec.examples


import com.beautiful.api.writable.WritableValue
import com.beautiful.data.vec.filter.BaseColumnFilter
import com.google.common.collect.Range

/**
  *
  * @Description:添加一个测试类
  * @Author: zhuyuping
  * @CreateDate: 2018/3/22 上午12:10
  *
  **/
class RangeMinMaxFilter(columnName: String, params: Map[String, AnyVal]) extends BaseColumnFilter(columnName) {

  require(params != null, "params is not allow null")
  require(params.contains("min"), "min param value is not allow null")
  require(params.contains("max"), "max param value is not allow null")

  val includeLower: Boolean = params.getOrElse("includeLower", true).asInstanceOf
  val includeUpper: Boolean = params.getOrElse("includeUpper", true).asInstanceOf
  var range: Range[Numeric] = _

  if (includeLower && includeUpper) {
    range = Range.closed[Numeric](params.get("min").get.asInstanceOf, params.get("max").get.asInstanceOf)
  } else if (!includeLower && includeUpper) {
    range = Range.openClosed[Numeric](params.get("min").get.asInstanceOf, params.get("max").get.asInstanceOf)
  } else if (!includeUpper && includeLower) {
    range = Range.closedOpen[Numeric](params.get("min").get.asInstanceOf, params.get("max").get.asInstanceOf)
  } else {
    range = Range.open[Numeric](params.get("min").get.asInstanceOf, params.get("max").get.asInstanceOf)
  }


  override def removeByColumn(writableValue: WritableValue): Boolean = {
    if (writableValue.get() == null) {
      false
    } else {
      require(writableValue.get().isInstanceOf[Numeric])
      val value = writableValue.get().asInstanceOf[Numeric]
      range.contains(value)
    }
  }


}

object RangeMinMaxFilter {

  def apply(columnName: String, map: Map[String, AnyVal]): RangeMinMaxFilter = new RangeMinMaxFilter(columnName, map)


}