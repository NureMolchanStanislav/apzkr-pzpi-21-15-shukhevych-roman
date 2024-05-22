namespace Application.Models.UpdateDtos;

public class NotificationUpdatedDto
{
    public int Id { get; set; }
    
    public int Condition { get; set; }
    
    public string Title { get; set; }
    
    public string Description { get; set; }
}