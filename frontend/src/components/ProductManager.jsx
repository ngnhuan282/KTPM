import React, { useEffect, useState } from "react";
import ProductList from "./ProductList";
import ProductForm from "./ProductForm";
import ProductDetail from "./ProductDetail";
import api from "../services/api";

export default function ProductManager() {
  const [page, setPage] = useState("list");
  const [products, setProducts] = useState([]);
  const [selected, setSelected] = useState(null);

  const loadData = async () => {
    try {
      const res = await api.get("/api/products"); // GET http://localhost:8080/api/products
      setProducts(res.data);
    } catch (err) {
      console.error("Load products error:", err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreate = () => {
    setSelected(null);
    setPage("form");
  };

  const handleEdit = (item) => {
    setSelected(item);
    setPage("form");
  };

  const handleView = (item) => {
    setSelected(item);
    setPage("detail");
  };

  const handleSave = async (product) => {
    try {
      if (product.id) {
        // UPDATE: PUT /api/products/{id}
        await api.put(`/api/products/${product.id}`, product);
      } else {
        // CREATE: POST /api/products
        await api.post("/api/products", product);
      }
      await loadData();
      setPage("list");
    } catch (err) {
      console.error("Save product error:", err);
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/api/products/${id}`);
      await loadData();
    } catch (err) {
      console.error("Delete product error:", err);
    }
  };

  return (
    <>
      {page === "list" && (
        <ProductList
          data={products}
          onCreate={handleCreate}
          onEdit={handleEdit}
          onDelete={handleDelete}
          onView={handleView}
        />
      )}

      {page === "form" && (
        <ProductForm
          product={selected}
          onSave={handleSave}
          onCancel={() => setPage("list")}
        />
      )}

      {page === "detail" && (
        <ProductDetail product={selected} onBack={() => setPage("list")} />
      )}
    </>
  );
}
