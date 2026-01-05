// rfce
// rfc --> ilma expordita lõpus
// rafce --> arrow function
// rafc --> ilma exportida lõpus arrow function
import { ChangeEvent, useContext, useEffect, useState } from 'react'
import type { Product } from '../models/Product';
import { OrderRow } from '../models/OrderRow';
import { CartSumContext } from '../context/CartSumContext';

function HomePage() {
  // const dbProducts: Product[] = [];

  const [products, setProducts] = useState<Product[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(2);
  const [sort, setSort] = useState("id,asc");
  const [loading, setLoading] = useState(true);
  const {cartSum, setCartSum} = useContext(CartSumContext);

  // uef. variant 1: fetch().then().then()
  // useEffect(() => {
  //   fetch(import.meta.env.VITE_BACKEND_URL + "/products")
  //     .then(res => res.json())
  //     .then(json => setProducts(json))
  //     .catch(error => console.log(error))
  // }, []);

  // uef. variant 2: async   await
  useEffect(() => {
    const load = async() => {
      try {
        const res = await fetch(import.meta.env.VITE_BACKEND_URL + `/products?size=${size}&page=${page}&sort=${sort}`);
        const json = await res.json();
        setProducts(json.content);
        setTotalPages(json.totalPages);
      } catch(error) {
        console.log(error);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [page, size, sort]); // nende muutujate muutumisel tehakse see useEffect uuesti

// Objects are not valid as a React child (found: object with keys {id, name, description, price, quantity, category}). If you meant to render a collection of children, use an array instead.

  function updateSize(e: ChangeEvent<HTMLSelectElement>) {
    setSize(Number(e.target.value));
    setPage(0);
  }

  function addToCart(product: Product) {
    const cartLS: OrderRow[] = JSON.parse(localStorage.getItem("cart") || "[]");
    const index = cartLS.findIndex(orderRow => orderRow.product.id === product.id);
    if (index >= 0) {
      cartLS[index].quantity++;
    } else {
      cartLS.push({"product": product, "quantity": 1});
    }
    localStorage.setItem("cart", JSON.stringify(cartLS));
    setCartSum(cartSum + product.price);
  }

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div>
      <button onClick={() => setSort("name,asc")}>Sorteeri A-Z</button>
      <button onClick={() => setSort("name,desc")}>Sorteeri Z-A</button>
      <button onClick={() => setSort("price,asc")}>Sorteeri hind kasvavalt</button>
      <button onClick={() => setSort("price,desc")}>Sorteeri hind kahanevalt</button>
      <button onClick={() => setSort("id,asc")}>Sorteeri vanemad ees</button>
      <button onClick={() => setSort("id,desc")}>Sorteeri uuemad ees</button>

      <br /><br />

      <label>Mitu tk nähtaval:</label>
      <select defaultValue={2} onChange={updateSize}>
        <option>1</option>
        <option>2</option>
        <option>3</option>
        <option>4</option>
      </select>

      <div className="products">
        {products.map(product => 
          <div key={product.id} className="product">
            <div>{product.name}</div>
            <div>{product.price}€</div>
            <button onClick={() => addToCart(product)}>Lisa ostukorvi</button>
          </div>)}
      </div>

      <button disabled={page === 0} onClick={() => setPage(page - 1)}>Eelmine</button>
      <span>{page+1}</span>
      <button disabled={page+1 >= totalPages} onClick={() => setPage(page + 1)}>Järgmine</button>
    </div>
  )
}

export default HomePage