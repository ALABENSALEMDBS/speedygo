using ProduitOrder.Models;
using ProduitOrder.repository;

namespace ProduitOrder.Service
{
    public class ProductService : IProductService
    {
        private readonly IProductRepository _repo;

        public ProductService(IProductRepository repo)
        {
            _repo = repo;
        }

        public async Task<Product> AddProduct(Product p)
        {
            await _repo.Add(p);
            return p;
        }

        public async Task<Product> UpdateProduct(Product p)
        {
            var existing = await _repo.GetById(p.Id);
            if (existing == null)
                throw new Exception("Produit introuvable pour mise à jour");

            p.PartnerName = existing.PartnerName;
            p.PreviousSales = existing.PreviousSales;
            p.Status = ProductStatus.APPROVED;

            await _repo.Update(p);
            return p;
        }

        public async Task DeleteProduct(string id)
            => await _repo.Delete(id);

        public async Task<Product> GetProduct(string id)
            => await _repo.GetById(id);

        public async Task<List<Product>> ListProducts()
            => await _repo.FindByStatus(ProductStatus.APPROVED);

        public async Task<List<Product>> GetProductsByCategory(string category)
        {
            if (!Enum.TryParse<Category>(category.ToUpper(), out var categoryEnum))
                return new List<Product>();

            return await _repo.FindByCategory(categoryEnum);
        }

        public async Task<List<Product>> GetPendingProducts()
            => await _repo.FindByStatus(ProductStatus.PENDING);

        public async Task<Product> ApproveProduct(string id)
        {
            var p = await _repo.GetById(id) ?? throw new Exception("Produit introuvable");
            p.Status = ProductStatus.APPROVED;
            await _repo.Update(p);
            return p;
        }

        public async Task<Product> RejectProduct(string id)
        {
            var p = await _repo.GetById(id) ?? throw new Exception("Produit introuvable");
            p.Status = ProductStatus.REJECTED;
            await _repo.Update(p);
            return p;
        }

        public async Task<List<Product>> GetProductsForPartner(string partnerName)
            => await _repo.FindByPartnerNameAndStatus(partnerName, ProductStatus.APPROVED);

        public Task<List<Product>> GetAll()
        {
            throw new NotImplementedException();
        }

        public Task<Product> GetById(string id)
        {
            throw new NotImplementedException();
        }

        public Task Add(Product product)
        {
            throw new NotImplementedException();
        }

        public Task Update(Product product)
        {
            throw new NotImplementedException();
        }

        public Task Delete(string id)
        {
            throw new NotImplementedException();
        }

        public Task<List<Product>> FindByCategory(Category category)
        {
            throw new NotImplementedException();
        }

        public Task<List<Product>> FindByStatus(ProductStatus status)
        {
            throw new NotImplementedException();
        }

        public Task<List<Product>> FindByCategoryAndStatus(Category category, ProductStatus status)
        {
            throw new NotImplementedException();
        }

        public Task<List<Product>> FindByPartnerNameAndStatus(string partnerName, ProductStatus status)
        {
            throw new NotImplementedException();
        }
    }
}
