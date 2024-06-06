namespace Application.Models.Dtos;

public class RFIDTagDto
{
    public string Id { get; set; }
    
    public bool Status { get; set; }
    
    public string ItemId { get; set; }
}

public class RFIDGetDto
{
    public string Id { get; set; }
    
    public string TagId { get; set; }
    
    public string ItemId { get; set; }
}