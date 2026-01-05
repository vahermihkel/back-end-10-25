import { useState } from "react"
import type { Category } from "../../models/Category";
import type { Product } from "../../models/Product";
import { ToastContainer, toast } from 'react-toastify';
import useFetch from "../../hooks/useFetch";
import useLoadItems from "../../hooks/useLoadItems";

// rfce
function AddProduct() {
  const [product, setProduct] = useState<Product>({
      "name": "", 
      "price": 0, 
      "description": "",
      "quantity": 0,
      "category": {
        "id": 1,
        "name": ""
      }
    });
  const backendQuery = useFetch();
  const {items: categories} = useLoadItems<Category>("/categories", false);

  const add = async() => {
    if (product.name === "") {
      toast.error("Cannot add without name");
      return;
    }
    if (product.price <= 0) {
      toast.error("Price cannot be negative");
      return;
    }
    backendQuery("/products", "POST", product, "added-product");
  }

  return (
    <div>
      <div>Ajutine väljakuvamine: {JSON.stringify(product)}</div>
      <label>Name</label> <br />
      <input onChange={(e) => setProduct({...product, "name": e.target.value})} type="text" /> <br />
      <label>Price</label> <br />
      <input onChange={(e) => setProduct({...product, "price": Number(e.target.value)})} type="number" /> <br />
      <label>Description</label> <br />
      <input onChange={(e) => setProduct({...product, "description": e.target.value})} type="text" /> <br />
      <label>Quantity</label> <br />
      <input onChange={(e) => setProduct({...product, "quantity": Number(e.target.value)})} type="number" /> <br />
      <label>Category</label> <br />
      {/* <input type="text" /> <br /> */}
      <select onChange={(e) => setProduct({...product, "category": {"id": Number(e.target.value), "name": ""}})}>
        {
          categories.map(category => 
            <option key={category.id} value={category.id}>{category.name}</option>
          )
        }
      </select> <br />
      <button onClick={add}>Add</button>

      <ToastContainer />
    </div>
  )
}

export default AddProduct