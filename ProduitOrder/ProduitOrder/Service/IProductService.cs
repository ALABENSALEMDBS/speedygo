using ProduitOrder.Models;

namespace ProduitOrder.Service
{
    public interface IProductService
    {
        Task<Product> AddProduct(Product p);
        Task<Product> UpdateProduct(Product p);
        Task DeleteProduct(string id);
        Task<Product> GetProduct(string id);
        Task<List<Product>> ListProducts();
        Task<List<Product>> GetProductsByCategory(string category);
        Task<List<Product>> GetPendingProducts();
        Task<Product> ApproveProduct(string id);
        Task<Product> RejectProduct(string id);
        Task<List<Product>> GetProductsForPartner(string partnerName);
    }
}
