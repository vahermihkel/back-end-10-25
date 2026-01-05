import useLoadItems from "../../hooks/useLoadItems";
import { Order } from "../../models/Order";

function MyOrders() {
  // const [orders, setOrders] = useState([]);
  const {items: orders, loading} = useLoadItems<Order>("/my-orders", true);

  if (loading) {
    return <div></div>
  }

  if (orders.length === 0) {
    return <div>Ühtegi tellimust veel tehtud ei ole</div>
  }

  return (
    <div>
      {orders.map(order => 
        <div key={order.id}>
          <div>{order.id}</div>
          <div>{order.created.toString()}</div>
          <div>{order.total}</div>
          <div>{order.orderRows.map(row => 
            <span key={row.id}>
              <span>{row.product.name} </span>
              <span>{row.quantity} tk | </span>
            </span>
          )}</div>
        </div>
      )}
    </div>
  )
}

export default MyOrders