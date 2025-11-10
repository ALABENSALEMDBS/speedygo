using ProduitOrder.Models;
using ProduitOrder.repository;

namespace ProduitOrder.Service
{
    public class OrderService : IOrderService
    {
        private readonly IOrderRepository _orderRepo;
        private readonly IProductRepository _productRepo;
        private readonly UserServiceClient _userClient;

        public OrderService(IOrderRepository orderRepo, IProductRepository productRepo, UserServiceClient userClient)
        {
            _orderRepo = orderRepo;
            _productRepo = productRepo;
            _userClient = userClient;
        }

       

        public async Task<Order> CreateOrder(Order order)
        {
            try
            {
                Console.WriteLine($"🔍 ORDER SERVICE: Starting order creation for user {order.UserId}");

                // Récupérer l'utilisateur
                Console.WriteLine($"🔍 ORDER SERVICE: Calling UserService for user {order.UserId}");
                var user = await _userClient.GetUserByIdAsync(order.UserId);

                if (user == null)
                {
                    Console.WriteLine($"❌ ORDER SERVICE: User {order.UserId} not found");
                    throw new Exception($"Utilisateur avec ID {order.UserId} introuvable");
                }

                Console.WriteLine($"✅ ORDER SERVICE: User found: {user.FirstName} {user.LastName}");

                order.UserFirstName = user.FirstName;
                order.UserLastName = user.LastName;

                Console.WriteLine($"🔍 ORDER SERVICE: Processing {order.Items.Count} items");

                // Traiter chaque item
                foreach (var item in order.Items)
                {
                    Console.WriteLine($"🔍 ORDER SERVICE: Checking product {item.ProductId}");

                    var product = await _productRepo.GetById(item.ProductId);
                    if (product == null)
                    {
                        Console.WriteLine($"❌ ORDER SERVICE: Product {item.ProductId} not found");
                        throw new Exception($"Produit introuvable : {item.ProductId}");
                    }

                    Console.WriteLine($"✅ ORDER SERVICE: Product found: {product.Name}");

                    if (product.StockQuantity < item.Quantity)
                    {
                        Console.WriteLine($"❌ ORDER SERVICE: Insufficient stock for {product.Name}. Available: {product.StockQuantity}, Requested: {item.Quantity}");
                        throw new Exception($"Stock insuffisant pour {product.Name}");
                    }

                    product.StockQuantity -= item.Quantity;
                    product.PreviousSales += item.Quantity;

                    Console.WriteLine($"🔍 ORDER SERVICE: Updating product stock");
                    await _productRepo.Update(product);
                }

                Console.WriteLine($"✅ ORDER SERVICE: All checks passed, creating order");
                return await _orderRepo.Add(order);
            }
            catch (Exception ex)
            {
                Console.WriteLine($"💥 ORDER SERVICE: Exception: {ex.Message}");
                Console.WriteLine($"💥 ORDER SERVICE: StackTrace: {ex.StackTrace}");
                throw;
            }
        }

        public async Task<Order> GetOrderById(string id)
            => await _orderRepo.GetById(id);

        public async Task<List<Order>> GetOrdersByUserId(long userId)
            => await _orderRepo.GetOrdersByUserId(userId);

        // Implémentation cohérente pour les autres méthodes
        public Task<Order> GetById(string id)
            => _orderRepo.GetById(id);

        public Task Update(Order order)
            => _orderRepo.Update(order);

        public Task<List<Order>> GetOrdersByUserId(string userId)
        {
            // Convertir string en long si nécessaire
            if (long.TryParse(userId, out long userIdLong))
            {
                return _orderRepo.GetOrdersByUserId(userIdLong);
            }
            throw new ArgumentException("ID utilisateur invalide");
        }
    }
}