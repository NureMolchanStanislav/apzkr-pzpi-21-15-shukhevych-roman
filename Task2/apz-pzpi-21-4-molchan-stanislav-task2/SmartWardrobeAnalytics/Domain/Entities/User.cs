using Domain.Common;

namespace Domain.Entities;

public class User : EntityBase 
{
    public string? FirstName { get; set; }

    public string? LastName { get; set; }

    public List<Role> Roles { get; set; }

    public string? Phone { get; set; }

    public string? Email { get; set; }

    public string? PasswordHash { get; set; }
    
    public string? City { get; set; }
    
    public string? Country { get; set; }
}