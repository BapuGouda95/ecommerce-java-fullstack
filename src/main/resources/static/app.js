let products=[], cart=JSON.parse(localStorage.getItem('shopsphere-cart')||'[]');
const tokenKey='shopsphere-token', userKey='shopsphere-user';
const grid=document.querySelector('#grid'),count=document.querySelector('#count'),cartBox=document.querySelector('#cartItems'),total=document.querySelector('#total');

const auth=()=>localStorage.getItem(tokenKey);
function user(){try{return JSON.parse(localStorage.getItem(userKey)||'null')}catch{return null}}
async function api(url,options={}){
  const headers={'Content-Type':'application/json',...(options.headers||{})};
  if(auth()) headers.Authorization='Bearer '+auth();
  const r=await fetch(url,{...options,headers});
  const data=await r.json().catch(()=>({}));
  if(!r.ok) throw new Error(data.error||'Request failed');
  return data;
}
async function load(){
  try{
    const q=document.querySelector('#search').value.trim(),c=document.querySelector('#category').value;
    const p=new URLSearchParams();if(q)p.set('search',q);if(c)p.set('category',c);
    products=await api('/api/products?'+p);render();
  }catch(e){grid.innerHTML='<p>Unable to load products. Start the Spring Boot application and check MySQL.</p>';}
}
function render(){
  grid.innerHTML=products.map(p=>`<article class="card">
    <img src="${escapeHtml(p.imageUrl||'')}" alt="${escapeHtml(p.name)}" onerror="this.style.display='none'">
    <div class="card-body"><span class="tag">${escapeHtml(p.category)}</span><h3>${escapeHtml(p.name)}</h3>
    <p>${escapeHtml(p.description||'')}</p><div class="stock">${p.stock>0?p.stock+' in stock':'Out of stock'}</div>
    <div class="price">₹${Number(p.price).toLocaleString('en-IN')}</div>
    <button class="add" ${p.stock<1?'disabled':''} onclick="add(${p.id})">${p.stock<1?'Out of stock':'Add to cart'}</button></div></article>`).join('')||'<p>No products found.</p>';
  renderCart();
}
function add(id){const p=products.find(x=>x.id===id);if(!p)return;const item=cart.find(x=>x.id===id);if(item&&item.qty>=p.stock)return toast('No more stock available');if(item)item.qty++;else cart.push({...p,qty:1});save();toast('Added to cart')}
function save(){localStorage.setItem('shopsphere-cart',JSON.stringify(cart));renderCart()}
function renderCart(){
  count.textContent=cart.reduce((s,x)=>s+x.qty,0);
  cartBox.innerHTML=cart.map(x=>`<div class="cart-row"><img src="${escapeHtml(x.imageUrl||'')}" alt=""><div style="flex:1"><b>${escapeHtml(x.name)}</b><div>₹${Number(x.price).toLocaleString('en-IN')}</div><div class="qty"><button onclick="change(${x.id},-1)">−</button> ${x.qty} <button onclick="change(${x.id},1)">+</button></div></div></div>`).join('')||'<p class="muted">Your cart is empty.</p>';
  total.textContent='₹'+cart.reduce((s,x)=>s+Number(x.price)*x.qty,0).toLocaleString('en-IN');
}
function change(id,n){const x=cart.find(i=>i.id===id);if(!x)return;x.qty+=n;if(x.qty<=0)cart=cart.filter(i=>i.id!==id);save()}
function toggleCart(){document.querySelector('#cart').classList.toggle('open')}
document.querySelector('#cartBtn').onclick=toggleCart;
document.querySelector('#search').oninput=load;document.querySelector('#category').onchange=load;
function openModal(id){document.querySelector('#'+id).classList.add('open')}
function closeModal(id){document.querySelector('#'+id).classList.remove('open')}
function showAuth(mode){
  const reg=mode==='register';document.querySelector('#authName').classList.toggle('hidden',!reg);
  document.querySelector('#authName').required=reg;document.querySelector('#loginTab').classList.toggle('active',!reg);document.querySelector('#registerTab').classList.toggle('active',reg);
  document.querySelector('#authSubmit').textContent=reg?'Create account':'Login';document.querySelector('#authForm').dataset.mode=mode;document.querySelector('#authMessage').textContent='';
}
document.querySelector('#authBtn').onclick=()=>{showAuth('login');openModal('authModal')};
document.querySelector('#authForm').onsubmit=async e=>{
 e.preventDefault();const mode=e.currentTarget.dataset.mode||'login';
 try{const body=mode==='register'?{name:authName.value.trim(),email:authEmail.value.trim(),password:authPassword.value}:{email:authEmail.value.trim(),password:authPassword.value};
  const data=await api('/api/auth/'+mode,{method:'POST',body:JSON.stringify(body)});localStorage.setItem(tokenKey,data.token);localStorage.setItem(userKey,JSON.stringify(data));closeModal('authModal');updateAuth();toast(mode==='register'?'Account created':'Welcome back');load();
 }catch(err){document.querySelector('#authMessage').textContent=err.message}
};
function updateAuth(){
 const u=user(),logged=!!u;document.querySelector('#authBtn').textContent=logged?'Logout':'Login';
 document.querySelector('#ordersBtn').classList.toggle('hidden',!logged);document.querySelector('#adminLink').classList.toggle('hidden',u?.role!=='ADMIN');
}
document.querySelector('#authBtn').addEventListener('click',()=>{if(user()){localStorage.removeItem(tokenKey);localStorage.removeItem(userKey);updateAuth();toast('Logged out')}});
document.querySelector('#ordersBtn').onclick=()=>{document.querySelector('#ordersSection').classList.remove('hidden');loadOrders();document.querySelector('#ordersSection').scrollIntoView({behavior:'smooth'})};
async function loadOrders(){
 try{const orders=await api('/api/orders/my');document.querySelector('#ordersList').innerHTML=orders.length?orders.map(o=>`<article class="order"><div class="order-top"><b>Order #${o.id}</b><span class="status ${o.status.toLowerCase()}">${o.status}</span></div><div class="muted">${new Date(o.createdAt).toLocaleString()} · ${escapeHtml(o.shippingAddress)}</div><div class="order-items">${o.items.map(i=>`<span>${escapeHtml(i.productName)} × ${i.quantity}</span>`).join('')}</div><strong>₹${Number(o.totalAmount).toLocaleString('en-IN')}</strong></article>`).join(''):'<p class="muted">No orders yet.</p>'}catch(e){toast(e.message)}
}
function openCheckout(){if(!cart.length)return toast('Your cart is empty');if(!auth())return openModal('authModal');openModal('checkoutModal')}
document.querySelector('#checkoutForm').onsubmit=async e=>{
 e.preventDefault();try{
  const data=await api('/api/orders',{method:'POST',body:JSON.stringify({shippingAddress:document.querySelector('#address').value.trim(),items:cart.map(x=>({productId:x.id,quantity:x.qty}))})});
  cart=[];save();closeModal('checkoutModal');document.querySelector('#address').value='';toast('Order #'+data.id+' placed successfully');loadOrders();
 }catch(err){toast(err.message);load()}
};
function toast(m){const t=document.querySelector('#toast');t.textContent=m;t.classList.add('show');setTimeout(()=>t.classList.remove('show'),2200)}
function escapeHtml(v){return String(v).replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]))}
updateAuth();showAuth('login');load();