package dummy

import ScalaSql.*
import SqlMacro.*

case class Order(
                orderNo: Int,
                orderDate: String,
                orderTime: String,
                amount: Int
                )

@main
def test: ResultSetMapper[Order] = resultSetMapper[Order]