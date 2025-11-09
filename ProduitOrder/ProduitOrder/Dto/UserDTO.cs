namespace ProduitOrder.Dto
{
    public class UserDTO
    {
        public long Id { get; set; }
        public string? Email { get; set; }
        public string? FirstName { get; set; }
        public string? LastName { get; set; }
        public bool Active { get; set; }
        public List<string>? Roles { get; set; }
    }
}
