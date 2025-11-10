using ProduitOrder.Models;

namespace ProduitOrder.repository
{
    public interface IProductRepository
    {
        Task<List<Product>> GetAll();
        Task<Product> GetById(string id);
        Task Add(Product product);
        Task Update(Product product);
        Task Delete(string id);

        Task<List<Product>> FindByCategory(Category category);
        Task<List<Product>> FindByStatus(ProductStatus status);
        Task<List<Product>> FindByCategoryAndStatus(Category category, ProductStatus status);
        Task<List<Product>> FindByPartnerNameAndStatus(string partnerName, ProductStatus status);
    }
}
