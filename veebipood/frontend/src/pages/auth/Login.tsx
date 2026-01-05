import { useContext, useState } from "react"
import { AuthContext } from "../../context/AuthContext";
import { ToastContainer, toast } from 'react-toastify';

function Login() {
  const [loginCredentials, setLoginCredentials] = useState({email: "", password: ""});
  const {login} = useContext(AuthContext);

  async function loginHandler() {
        if (loginCredentials.email === "") {
          toast.error("Cannot add without email");
          return;
        }
        if (loginCredentials.password === "") {
          toast.error("Cannot add without password");
          return;
        }
        try {
          const res = await fetch(import.meta.env.VITE_BACKEND_URL + "/login", {
            method: "POST",
            body: JSON.stringify(loginCredentials),
            headers: {
              "Content-Type": "application/json"
            }
          });
          const json = await res.json();
          if (json.message && json.status && json.timestamp) {
            toast.error(json.message);
          } else {
            login(json.token);
            toast.success("Edukalt sisse logitud!");
          }
        } catch(error) {
          toast.error(String(error));
        }
    }

  return (
    <div>
      <label>Email</label> <br />
      <input onChange={(e) => setLoginCredentials({...loginCredentials, "email": e.target.value})} type="text" /> <br />
      <label>Password</label> <br />
      <input onChange={(e) => setLoginCredentials({...loginCredentials, "password": e.target.value})} type="text" /> <br />
      <button onClick={loginHandler}>Login</button>
      <ToastContainer />
    </div>
  )
}

export default Login