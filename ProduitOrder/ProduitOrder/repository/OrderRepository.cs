using MongoDB.Driver;
using ProduitOrder.Models;

namespace ProduitOrder.repository
{
    public class OrderRepository:IOrderRepository
    {
        private readonly IMongoCollection<Order> _orders;

        public OrderRepository(IMongoDatabase database)
        {
            _orders = database.GetCollection<Order>("orders");
        }

        public async Task<Order> Add(Order order)
        {
            await _orders.InsertOneAsync(order);
            return order;
        }

        public async Task<Order> GetById(string id)
            => await _orders.Find(o => o.Id == id).FirstOrDefaultAsync();

        public async Task Update(Order order)
            => await _orders.ReplaceOneAsync(o => o.Id == order.Id, order);

        public async Task<List<Order>> GetOrdersByUserId(long userId)
     => await _orders.Find(o => o.UserId == userId).ToListAsync();

    }
}
