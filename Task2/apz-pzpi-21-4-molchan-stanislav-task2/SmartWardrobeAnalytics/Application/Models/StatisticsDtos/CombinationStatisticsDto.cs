using MongoDB.Bson;

namespace Application.Models.StatisticsDtos;

public class CombinationStatisticsDto
{
    public string UserId { get; set; }
    
    public List<string> Combination { get; set; }
    
    public int UsageCount { get; set; }
    
    public string Date { get; set; }
}