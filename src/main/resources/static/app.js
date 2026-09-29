let products=[],cart=JSON.parse(localStorage.getItem('shopsphere-cart')||'[]');
const grid=document.querySelector('#grid'),count=document.querySelector('#count'),cartBox=document.querySelector('#cartItems'),total=document.querySelector('#total');
async function load(){const q=document.querySelector('#search').value, c=document.querySelector('#category').value;const p=new URLSearchParams();if(q)p.set('search',q);if(c)p.set('category',c);products=await fetch('/api/products?'+p).then(r=>r.json());render();}
function render(){grid.innerHTML=products.map(p=>`<article class="card"><img src="${p.imageUrl}" alt="${p.name}"><div class="card-body"><span class="tag">${p.category}</span><h3>${p.name}</h3><p>${p.description}</p><div class="price">₹${Number(p.price).toLocaleString('en-IN')}</div><button class="add" onclick="add(${p.id})">Add to cart</button></div></article>`).join('')||'<p>No products found.</p>';renderCart();}
function add(id){const p=products.find(x=>x.id===id), item=cart.find(x=>x.id===id);if(item)item.qty++;else cart.push({...p,qty:1});save();toast('Added to cart');}
function save(){localStorage.setItem('shopsphere-cart',JSON.stringify(cart));renderCart();}
function renderCart(){count.textContent=cart.reduce((s,x)=>s+x.qty,0);cartBox.innerHTML=cart.map(x=>`<div class="cart-row"><img src="${x.imageUrl}"><div style="flex:1"><b>${x.name}</b><div>₹${Number(x.price).toLocaleString('en-IN')}</div><div class="qty"><button onclick="change(${x.id},-1)">−</button> ${x.qty} <button onclick="change(${x.id},1)">+</button></div></div></div>`).join('')||'<p>Your cart is empty.</p>';total.textContent='₹'+cart.reduce((s,x)=>s+Number(x.price)*x.qty,0).toLocaleString('en-IN');}
function change(id,n){const x=cart.find(i=>i.id===id);if(!x)return;x.qty+=n;if(x.qty<=0)cart=cart.filter(i=>i.id!==id);save();}
function toggleCart(){document.querySelector('#cart').classList.toggle('open')}
document.querySelector('#cartBtn').onclick=toggleCart;document.querySelector('#search').oninput=load;document.querySelector('#category').onchange=load;
function checkout(){if(!cart.length)return toast('Your cart is empty');toast('Demo checkout — order module is next');}
function toast(m){const t=document.querySelector('#toast');t.textContent=m;t.style.display='block';setTimeout(()=>t.style.display='none',1800)}
load();