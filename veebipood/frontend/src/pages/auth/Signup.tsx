import { useState } from "react"
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from 'react-toastify';

function Signup() {
  const [person, setPerson] = useState({email: "", password: "", firstName: "", lastName: "", role: "CUSTOMER"});
  const navigate = useNavigate();

  async function signupHandler() {
      if (person.email === "") {
        toast.error("Cannot add without email");
        return;
      }
      if (person.password === "") {
        toast.error("Cannot add without password");
        return;
      }
      try {
        const res = await fetch(import.meta.env.VITE_BACKEND_URL + "/signup", {
          method: "POST",
          body: JSON.stringify(person),
          headers: {
            "Content-Type": "application/json"
          }
        });
        const json = await res.json();
        if (json.message && json.status && json.timestamp) {
          toast.error(json.message);
        } else {
          toast.success("Edukalt  registreerutud!");
          navigate("/login");
        }
      } catch(error) {
        toast.error(String(error));
      }
  }

  return (
    <div>
      <label>First name</label> <br />
      <input onChange={(e) => setPerson({...person, "firstName": e.target.value})} type="text" /> <br />
      <label>Last name</label> <br />
      <input onChange={(e) => setPerson({...person, "lastName": e.target.value})} type="text" /> <br />
      <label>Email</label> <br />
      <input onChange={(e) => setPerson({...person, "email": e.target.value})} type="text" /> <br />
      <label>Password</label> <br />
      <input onChange={(e) => setPerson({...person, "password": e.target.value})} type="password" /> <br />
      <label>ROLE (for testing purposes)</label> <br />
      <input onChange={(e) => setPerson({...person, "role": e.target.value})} type="text" /> <br />
      <button onClick={signupHandler}>Sign up</button>
      <ToastContainer />
    </div>
  )
}

export default Signup