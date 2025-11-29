import React, { useEffect, useState } from "react";
import ProductList from "./ProductList";
import ProductForm from "./ProductForm";
import ProductDetail from "./ProductDetail";
import api from "../services/api";

export default function ProductManager() {
    const [page, setPage] = useState("list");
    const [products, setProducts] = useState([]);
    const [selected, setSelected] = useState(null);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const loadData = async () => {
        try {
            const res = await api.get("/api/products"); // GET http://localhost:8080/api/products
            setProducts(res.data);
        } catch (err) {
            console.error("Load products error:", err);
            setError("Không thể tải danh sách sản phẩm. Vui lòng thử lại.");
            setMessage("");
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const showErrorFromApi = (err) => {
        console.error("API error:", err);

        const apiMessage = err?.response?.data;

        if (typeof apiMessage === "string" && apiMessage.trim() !== "") {
            // Ví dụ: "Tên sản phẩm đã tồn tại", "Loại sản phẩm không hợp lệ", "Sản phẩm không tồn tại"
            setError(apiMessage);
        } else {
            setError("Đã xảy ra lỗi. Vui lòng thử lại.");
        }

        setMessage("");
    };

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
            setError("");
            setMessage("");

            if (product.id == null) {
                // Tạo mới
                await api.post("/api/products", product);
                setMessage("Thêm sản phẩm thành công");
            } else {
                // Cập nhật
                await api.put(`/api/products/${product.id}`, product);
                setMessage("Cập nhật sản phẩm thành công");
            }

            await loadData();
            setPage("list");
        } catch (err) {
            showErrorFromApi(err); // Hiển thị "Tên sản phẩm đã tồn tại" / "Loại sản phẩm không hợp lệ"...
        }
    };

    const handleDelete = async (id) => {
        // Dialog xác nhận YES/NO
        const confirmed = window.confirm(
            "Bạn có chắc muốn xóa sản phẩm này không?"
        );
        if (!confirmed) {
            return; // Người dùng chọn Hủy
        }

        try {
            setError("");
            setMessage("");

            await api.delete(`/api/products/${id}`);

            await loadData();
            setMessage("Xóa sản phẩm thành công");
        } catch (err) {
            // Nếu BE trả "Sản phẩm không tồn tại" thì sẽ hiển thị đúng message đó
            showErrorFromApi(err);
        }
    };

    return (
        <div>
            {/* Thông báo chung */}
            {message && <p style={{ color: "green" }}>{message}</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

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
        </div>
    );
}
