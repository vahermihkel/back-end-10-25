import type { OrderRow } from "./OrderRow"

export type Order = {
  id?: number
  created: Date,
  total: number,
  // person: Person,
  orderRows: OrderRow[]
}