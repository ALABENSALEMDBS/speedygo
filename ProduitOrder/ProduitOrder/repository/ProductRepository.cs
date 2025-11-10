using MongoDB.Driver;
using ProduitOrder.Models;

namespace ProduitOrder.repository
{
    public class ProductRepository:IProductRepository

    {
        private readonly IMongoCollection<Product> _products;

        public ProductRepository(IMongoDatabase database)
        {
            _products = database.GetCollection<Product>("products");
        }

        public async Task<List<Product>> GetAll()
            => await _products.Find(_ => true).ToListAsync();

        public async Task<Product> GetById(string id)
            => await _products.Find(x => x.Id == id).FirstOrDefaultAsync();

        public async Task Add(Product product)
            => await _products.InsertOneAsync(product);

        public async Task Update(Product product)
            => await _products.ReplaceOneAsync(x => x.Id == product.Id, product);

        public async Task Delete(string id)
            => await _products.DeleteOneAsync(x => x.Id == id);

        public async Task<List<Product>> FindByCategory(Category category)
            => await _products.Find(x => x.Category == category).ToListAsync();

        public async Task<List<Product>> FindByStatus(ProductStatus status)
            => await _products.Find(x => x.Status == status).ToListAsync();

        public async Task<List<Product>> FindByCategoryAndStatus(Category category, ProductStatus status)
            => await _products.Find(x => x.Category == category && x.Status == status).ToListAsync();

        public async Task<List<Product>> FindByPartnerNameAndStatus(string partnerName, ProductStatus status)
            => await _products.Find(x => x.PartnerName == partnerName && x.Status == status).ToListAsync();
    }

}

