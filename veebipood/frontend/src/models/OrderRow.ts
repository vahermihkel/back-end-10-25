import type { Product } from "./Product"

export type OrderRow = {
  id?: number
  quantity: number,
  product: Product
}