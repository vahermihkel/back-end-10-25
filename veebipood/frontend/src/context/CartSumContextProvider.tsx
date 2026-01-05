import { type ReactNode, useState } from "react";
import { CartSumContext } from "./CartSumContext";
import { OrderRow } from "../models/OrderRow";

// globaalsuse määraja
export const CartSumContextProvider = ({children}: {children: ReactNode}) => {
  const [cartSum, setCartSum] = useState(calculateCartSum());

  function calculateCartSum() {
    const cartLS: OrderRow[] = JSON.parse(localStorage.getItem("cart") || "[]");
    let sum = 0;
    cartLS.forEach(row => sum += row.product.price * row.quantity);
    return sum;
  }

  return (
    <CartSumContext.Provider value={{cartSum, setCartSum}}>
      {children}
    </CartSumContext.Provider>
  )
}

// <Provider><Admin /></Provider> --> impordin Contexti ja küsin CartSumi --> 123
// <Cart /> impordin Contexti ja küsin CartSumi --> 0